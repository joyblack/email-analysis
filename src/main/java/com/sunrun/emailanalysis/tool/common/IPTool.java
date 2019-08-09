package com.sunrun.emailanalysis.tool.common;

public class IPTool {
    private static int BINARY_VALUE_1 = 256 * 256 * 256;

    private static int BINARY_VALUE_2 = 256 * 256 ;

    private static int BINARY_VALUE_3 = 256;

    private static int BINARY_VALUE_4 = 1;


    // Translate the ip string to long value.
    public static long ipToLong (String ip){
        if(ip == null){
            return 0;
        }
        try{
            String[] split = ip.split("\\.");
            long value = 0;
            value += Long.valueOf(split[0]) * BINARY_VALUE_1;
            value += Long.valueOf(split[1]) * BINARY_VALUE_2;
            value += Long.valueOf(split[2]) * BINARY_VALUE_3;
            value += Long.valueOf(split[3]) * BINARY_VALUE_4;
            return value;
        }catch (Exception e){
            System.out.println("Translate error: " + e.getMessage());
            return 0;
        }

    }
}
