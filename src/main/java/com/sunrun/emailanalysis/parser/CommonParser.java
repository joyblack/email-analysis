package com.sunrun.emailanalysis.parser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonParser {

    public enum ParserType{
        TYPE_NOT_SUPPORT,
        TYPE_EMAIL,
        TYPE_EXCEL,
        TYPE_HTML,
        TYPE_OTHER
    }

    private static List<String> notSupportExtension;

    private static List<String> emailExtension;

    private static List<String> excelExtension;

    private static List<String> htmlExtension;

    static{
        notSupportExtension = Arrays.asList(
                "jpg","png","gif","tif","bmp","dwg",
                "psd",
                "mp3","wmv",
                "mp4","mpg","wav","avi","flv",
                "mid",
                "zip","rar","jar","gz",
                "exe",
                "mf",
                "chm","mxp",""
        );

        emailExtension = Arrays.asList("eml","msg");

        excelExtension = Arrays.asList("xls","xlsx");

        htmlExtension = Arrays.asList("htm","html");
    }

    // 支持提取文本内容的后缀类型
    public static boolean isSupportExtractContent(String extension){
        return !notSupportExtension.contains(extension);
    }




    public static ParserType getParserType(String extension){
        // not support
        if(notSupportExtension.contains(extension)){
            return ParserType.TYPE_NOT_SUPPORT;
        }

        // email
        if(emailExtension.contains(extension)){
            return ParserType.TYPE_EMAIL;
        }

        // excel
        if(excelExtension.contains(extension)){
            return ParserType.TYPE_EXCEL;
        }

        // html
        if(htmlExtension.contains(extension)){
            return ParserType.TYPE_HTML;
        }

        return ParserType.TYPE_OTHER;

    }

    // Judge whether if parser this extension file.
    private static boolean isEmailFile(String extension){

        return false;
    }

    public static String filterBlack(String str){
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}
