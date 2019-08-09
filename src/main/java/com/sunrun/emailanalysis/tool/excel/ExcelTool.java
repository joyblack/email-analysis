package com.sunrun.emailanalysis.tool.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ExcelTool {
    // read excel all content.
    public static String getContent(String filePath) {
        try {
            ExcelTypeEnum type = ExcelTypeEnum.XLS;
            if(filePath.endsWith(".xlsx")){
                type = ExcelTypeEnum.XLSX;
            }
            // 解析每行结果在listener中处理
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(new FileInputStream(new File(filePath)), type,null, listener);
            excelReader.read();
            return listener.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }




}
