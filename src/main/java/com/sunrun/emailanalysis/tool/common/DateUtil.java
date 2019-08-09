package com.sunrun.emailanalysis.tool.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String format(Date date){
        return formatter.format(date);
    }
}
