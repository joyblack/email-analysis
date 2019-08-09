package com.sunrun.emailanalysis.joy.file.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileCombineExtension {
    public static final String EMAIL = "email";
    public static final String IMAGE = "image";
    public static final String PSD = "psd";
    public static final String MUSIC = "music";
    public static final String VIDEO = "video";
    public static final String WINRAR = "winrar";
    public static final String DOC = "doc";
    public static final String EXCEL = "excel";
    public static final String PPT = "ppt";
    public static final String OFFICE = "office";
    public static final String ALL = "all";

    private static final List<String> EMAIL_EXT = Arrays.asList("msg","eml");
    private static final List<String> IMAGE_EXT = Arrays.asList("jpg","png","gif","tif","bmp","dwg");
    private static final List<String> PSD_EXT = Arrays.asList("psd");
    private static final List<String> MUSIC_EXT = Arrays.asList("mp3","wmv");
    private static final List<String> VIDEO_EXT = Arrays.asList("mp4","mpg","wav","avi","flv");
    private static final List<String> WINRAR_EXT = Arrays.asList("zip","rar","jar","gz");
    private static final List<String> DOC_EXT = Arrays.asList("doc","docx");
    private static final List<String> EXCEL_EXT = Arrays.asList("xls","xlsx");
    private static final List<String> PPT_EXT = Arrays.asList("ppt","pptx");
    private static final List<String> OFFICE_EXT = Arrays.asList("doc","docx","xls","xlsx","ppt","pptx");
    private static final List<String> ALL_EXT = null;

    public static List<String> getExtensionList(String type) {
        switch (type.toLowerCase()) {
            case IMAGE: return IMAGE_EXT;
            case PSD: return PSD_EXT;
            case WINRAR: return WINRAR_EXT;
            case EMAIL: return EMAIL_EXT;
            case EXCEL: return EXCEL_EXT;
            case MUSIC: return MUSIC_EXT;
            case VIDEO: return VIDEO_EXT;
            case DOC: return DOC_EXT;
            case PPT: return PPT_EXT;
            case OFFICE: return OFFICE_EXT;
            case ALL: return ALL_EXT;
            // Other type, just think this is file extension.
            default: return Arrays.asList(type);
        }
    }

    // By big or small file type to combine all type to a small type list.
    public static List<String> getSmallFileTypeList(List<String> fileType){
        List<String> extList = new ArrayList<>();
        for (String type : fileType) {
            List<String> ext = getExtensionList(type);
            if(ext == null){
                return null;
            }else{
                extList.addAll(ext);
            }
        }
        return extList;
    }
}
