package com.sunrun.emailanalysis.dictionary.email;

import java.util.Arrays;
import java.util.List;

public class JoyAttachDictionary {
    // 支持要素提取的附件类型
    public static List<String> DOCUMENT_SUPPORTS = Arrays.asList(
            "csv", "txt", "html", "htm", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "eml", "msg", "odt", "pps", "ppsx"
    );

    // excel类型的附件
    public static List<String> DOCUMENT_EXCEL = Arrays.asList("xls","xlsx");

    // html类型的附件
    public static List<String> DOCUMENT_HTML = Arrays.asList("html");

    // email类型的附件
    public static List<String> DOCUMENT_EMAIL = Arrays.asList("eml");


}
