package com.sunrun.emailanalysis.tool.common;

public class EmailTool {
    public static String getDomainName(String address){
        try{
            if(address == null){
                return null;
            }
            address = address.trim();
            if(address.isEmpty()){
                return null;
            }
            // 1016037677@qq.com '@'
            String[] split = address.split("@");
            if(split.length == 2){
                return split[1];
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
