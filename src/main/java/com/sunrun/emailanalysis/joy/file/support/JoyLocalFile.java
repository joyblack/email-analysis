package com.sunrun.emailanalysis.joy.file.support;

import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.file.tool.JoySimpleFileTool;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class JoyLocalFile implements JoyFile {
    // store file to set target.
    @Override
    public void storeFile(InputStream in, String targetFile) throws Exception{
        // 读取附件字节并存储到文件中
        FileOutputStream out = new FileOutputStream(targetFile);
        byte[] b = new byte[1024];
        int length;
        while ((length = in.read(b)) != -1) {
            out.write(b, 0, length);
        }
        in.close();
        out.close();
        System.out.println("=== store attach file in " + targetFile + " success.");
    }

    // list all dir file.
    @Override
    public List<File> listFile(File dir, FileFilter filter, boolean rescue){
        List<File> lst = new ArrayList<File>();
        if(!dir.isDirectory()){
            return lst;
        }
        File[] children = dir.listFiles();
        for(int i = 0; i< children.length; i++){

            File file = children[i];
            if(filter.accept(file)){
                lst.add(file);
            }
            if(file.isDirectory() && rescue){
                lst.addAll(listFile(file, filter, true));
            }
        }
        return lst;
    }

    @Override
    public boolean mkdir(String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            return true;
        }else{
            return file.mkdirs();
        }
    }

    // 获取文件的输入流
    @Override
    public FileInputStream getFileInputStream(String filePath){
        try {
            return new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void download(String filePath, String fileName, HttpServletResponse response) {
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        // Get attach information.
        try{
            //bufferedInputStream = new BufferedInputStream(JoyFile.getFileInputStream(attach.getStorePath()));
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            // Response config.
            response.reset();
            //response.setContentLength(attach.getFileSize().intValue());
            response.setContentType("application/octet-stream");
            response.setHeader("Pragma","No-cache");
            response.setHeader("Cache-Control","No-cache");
            response.setDateHeader("Expires",0);
            response.setCharacterEncoding("utf-8");
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

            // Start download.
            outputStream = response.getOutputStream();
            byte[] buff = new byte[2048];
            int length;
            while((length = bufferedInputStream.read(buff)) != -1){
                outputStream.write(buff, 0, length);
                outputStream.flush();
            }

        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
                if(bufferedInputStream != null){
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteDir(String filePath){
        try {
            FileUtils.deleteDirectory(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
