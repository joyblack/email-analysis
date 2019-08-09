package com.sunrun.emailanalysis.service;


import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.EmailAttachMapper;
import com.sunrun.emailanalysis.mapper.EmailCaseMapper;
import com.sunrun.emailanalysis.po.EmailAttach;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.opencv.core.Core.bitwise_and;

@Service
public class OcrService {
    private static Logger log = LoggerFactory.getLogger(OcrService.class);

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;


    public EAResult ocrImgByAttachId(Long id) {


        HashMap<String, Object> attach = emailAttachMapper.getAttach(id);

        if (attach == null) {
            throw new EAException("没有该附件的信息", Notice.ATTACH_NOT_EXSIT);
        }

        Object fileType = attach.get("fileType");

        if (fileType != null) {
            String trimType = fileType.toString().trim();
            if (trimType != "jpg" || trimType != "png") {
                return EAResult.buildFailedResult(Notice.PATH_IS_NOT_EXIST, "提供的附件不是jpg或png类型");
            }
            log.info("The trimType is: {}", trimType);
            //获取附件路径
            Long attachId = Long.valueOf(attach.get("attachId").toString());
            EmailAttach emailAttach = emailAttachMapper.selectByPrimaryKey(attachId);
            String storePath = emailAttach.getStorePath();
            String ocrTxt = ocrStarter(storePath);
            //将解析结果存入数据库
            emailAttachMapper.updateByPrimaryKeySelective(emailAttach);
            return EAResult.buildSuccessResult();
        }

        return EAResult.buildFailedResult(Notice.PATH_IS_NOT_EXIST, "提供的附件不是有效附件");

    }

    public String ocrStarter(String attachPath) {
        String ocrTxt = getTextFromBillImages(attachPath);
        return ocrTxt;
    }

    private String getTextFromBillImages(final String billImageFilePath) {

        //加载opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //读取提单图像文件
        Mat srcImage = Imgcodecs.imread(billImageFilePath);

        //对图像进行灰度化、平滑滤波、二值化操作，得到提单的二值化图像
        Mat binaryImage = smoothImage(srcImage);

        //获取图像名
        String name = new File(billImageFilePath).getName().replace(".", "-");

        //创建存储切割后图像的临时文件夹
        File tempdir = new File(".\\ocr-temp\\tempImage-" + name);
        tempdir.mkdirs();

        //对二值化图像进行处理得到按表格边框切割后的图像
        findPoint(binaryImage, srcImage, tempdir);
        

        //创建线程池
        ExecutorService exe = Executors.newFixedThreadPool(10);

        OcrRunnable ocr = new OcrRunnable(tempdir);
        for (int i = 1; i <= 10; i++) {
            exe.execute(new Thread(ocr, "线程" + i));
        }

        exe.shutdown();

        try {
            while (true) {
                if (exe.isTerminated()) {
                    return ocr.getTxt();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Mat smoothImage(Mat srcImage) {

        Mat destImage = srcImage.clone();

        //灰度图
        Imgproc.cvtColor(srcImage, destImage, Imgproc.COLOR_RGB2GRAY);

        //中值滤波
        Imgproc.medianBlur(destImage, destImage, 7);

        //二值化
        Imgproc.adaptiveThreshold(destImage, destImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

        return destImage;
    }

    private void findPoint(Mat binaryImage, Mat srcImage, File tempFile) {
        Mat destImage = binaryImage.clone();

        //获取表格
        Mat horizontal = destImage.clone();
        Mat vertical = destImage.clone();

        int scale = 50; //这个值越大，检测到的直线越多
        int horizontalsize = horizontal.cols() / scale;
        int verticalsize = vertical.rows() / scale;

        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
        Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalsize));

        //先腐蚀再膨胀得到横线
        Imgproc.erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1));
        Imgproc.dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1));

        //先腐蚀再膨胀得到竖线
        Imgproc.erode(vertical, vertical, verticalStructure, new Point(-1, -1));
        Imgproc.dilate(vertical, vertical, verticalStructure, new Point(-1, -1));

        //合并得到表格
        Mat mask = new Mat();
        Core.add(horizontal, vertical, mask);

        //获取交点
        Mat points = new Mat();
        bitwise_and(horizontal, vertical, points);

        //获取轮廓
        findContour(mask, points, srcImage, tempFile);
    }

    private void findContour(Mat mask, Mat points, Mat srcImage, File tempFile) {

        //找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<MatOfPoint> contours_poly = contours;
        Rect[] boundRect = new Rect[contours.size()];
        LinkedList<Mat> tables = new LinkedList<Mat>();

        //找到所有的轮廓点
        for (int i = 0; i < contours.size(); i++) {

            MatOfPoint point = contours.get(i);
            MatOfPoint contours_poly_point = contours_poly.get(i);

            /*
             * 获取区域的面积
             * 第一个参数，InputArray contour：输入的点，一般是图像的轮廓点
             * 第二个参数，bool oriented = false:表示某一个方向上轮廓的的面积值，顺时针或者逆时针，一般选择默认false
             */
            double area = Imgproc.contourArea(contours.get(i));
            //如果小于某个值就忽略，代表是杂线不是表格
            if (area < 100) {
                continue;
            }
            /*
             * approxPolyDP 函数用来逼近区域成为一个形状，true值表示产生的区域为闭合区域。比如一个带点幅度的曲线，变成折线
             * MatOfPoint2f curve：像素点的数组数据。
             * MatOfPoint2f approxCurve：输出像素点转换后数组数据。
             * double epsilon：判断点到相对应的line segment 的距离的阈值。（距离大于此阈值则舍弃，小于此阈值则保留，epsilon越小，折线的形状越“接近”曲线。）
             * bool closed：曲线是否闭合的标志位。
             */
            Imgproc.approxPolyDP(new MatOfPoint2f(point.toArray()), new MatOfPoint2f(contours_poly_point.toArray()), 3, true);

            //为将这片区域转化为矩形，此矩形包含输入的形状
            boundRect[i] = Imgproc.boundingRect(contours_poly.get(i));

            // 找到交汇处的的表区域对象
            Mat table_image = points.submat(boundRect[i]);

            List<MatOfPoint> table_contours = new ArrayList<MatOfPoint>();
            Mat joint_mat = new Mat();
            Imgproc.findContours(table_image, table_contours, joint_mat, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
            //从表格的特性看，如果这片区域的点数小于4，那就代表没有一个完整的表格，忽略掉
            if (table_contours.size() < 4)
                continue;

            //保存图片
            tables.addFirst(srcImage.submat(boundRect[i]).clone());

            //将矩形画在原图上
            Imgproc.rectangle(srcImage, boundRect[i].tl(), boundRect[i].br(), new Scalar(0, 255, 0), 1, 8, 0);
        }


        for (int i = 0; i < tables.size() - 1; i++) {
            //拿到表格后，进行OCR 识别
            Imgcodecs.imwrite(tempFile.getAbsolutePath() + "\\AfterTable" + (i + 1) + ".jpg", tables.get(i + 1));
        }
    }
}

class OcrRunnable implements Runnable {
    List<File> filePathsList = new ArrayList<File>();
    int index = 0;
    private String txtFromImage = "";

    public OcrRunnable(File file) {
        getFileList(file);
    }

    private void getFileList(File f) {
        File[] filePaths = f.listFiles();
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (s.getName().lastIndexOf(".jpg") != -1) {
                    filePathsList.add(s);
                }
            }
        }
    }

    @Override
    public void run() {
        File file = null;
        while (index < filePathsList.size()) {
            synchronized (this) {
                if (index >= filePathsList.size()) {
                    continue;
                }
                file = filePathsList.get(index);
                index++;
            }

            try {
                this.txtFromImage += ocrImage(file);
                //System.out.println(txtFromImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTxt() {
        return this.txtFromImage;
    }

    public String ocrImage(File imageFile) throws IOException, InterruptedException {

        final String LANG_OPTION = "-l";  //英文字母小写l，并非数字1
        final String  c = System.getProperty("line.separator");
        //String tessPath = "C:\\Program Files (x86)\\Tesseract-OCR";

        File tempImage = imageFile;
        File outputFile = new File(imageFile.getAbsolutePath().replace(".jpg", ""));
        StringBuffer strB = new StringBuffer();
        List cmd = new ArrayList();
        //cmd.add(tessPath + "//tesseract");
        cmd.add("tesseract");
        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
        cmd.add("chi_sim");


        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());

        cmd.set(1, tempImage.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int w = process.waitFor();

        //删除临时正在工作文件
        tempImage.delete();

        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));

            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append("");
            }
            in.close();
        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "解析错误，文件名中可能有空格存在";
                    break;
                case 29:
                    msg = "无法识别图像";
                    break;
                case 31:
                    msg = "不支持此图像格式";
                    break;
                default:
                    msg = "发生错误";
            }
            tempImage.delete();
            throw new RuntimeException(msg);
        }
        new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString();
    }
}

