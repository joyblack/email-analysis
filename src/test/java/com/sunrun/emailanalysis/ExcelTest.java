package com.sunrun.emailanalysis;

import com.sunrun.emailanalysis.tool.excel.ExcelTool;
import org.junit.Test;

public class ExcelTest {
    @Test
    public void test(){
        String storePath = "test/delivery_number.xls";
        System.out.println(ExcelTool.getContent(storePath));
    }
}
