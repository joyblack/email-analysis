package com.sunrun.emailanalysis;

import com.spreada.utils.chinese.ZHConverter;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CommonTest {
    @Test
    public void test(){
        System.out.println("扎");
        // 繁体转简体
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
        String simplifiedStr = converter.convert("喝水和氣身材興奮生意");
        String simplifiedStr2 = converter.convert("業園溫泉度假村%￥……");
        System.out.println(simplifiedStr);
        System.out.println(simplifiedStr2);

        // 简体转繁体
        ZHConverter converter2 = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
        String traditionalStr = converter2.convert("长隆饭店广州广东电视台");
        System.out.println(traditionalStr);
    }


    @Test
    public void test2(){
        String fileName = "txt";
        String[] strArray = fileName.split("\\.");
        System.out.println(strArray[strArray.length - 1]);
    }

    @Test
    public void test3(){
        File inputFile = new File("/ea/test");
        if(inputFile.isDirectory()){
            System.out.println("this is dir.");
        }else if(inputFile.isFile()){
            System.out.println("this is a file.");
        }else{
            System.out.println("nothing!");
        }
    }

    @Test
    public void luceneTest(){
        System.out.println("你是".hashCode());
        System.out.println("你是".hashCode());
        String s = "你是谁啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊sssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
        String t = "你是谁啊啊啊";
        System.out.println("你是谁啊啊啊".hashCode());
        System.out.println(s.hashCode());
        System.out.println(t.hashCode());
    }

    @Test
    public void tt(){
        DecimalFormat df = new DecimalFormat(".00");
        long completed = 100;
        String format = df.format(( completed) / 250 * 100);

        System.out.println(format);

    }

    @Test
    public void tt1(){
        List<String> a = new ArrayList<>();

        System.out.println(a.toString().replace("[","").replace("]",""));
    }


}
