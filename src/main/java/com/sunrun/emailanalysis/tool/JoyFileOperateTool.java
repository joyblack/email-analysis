package com.sunrun.emailanalysis.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JoyFileOperateTool {
    // 批量复制文件
    public static void copyDirectory(Path from, Path to) throws Exception {
        // == 检测源目录是否存在
        if(!Files.exists(from)){
            throw new FileNotFoundException("source file is not exist.");
        }
        // == check target folder exist, if not, create it.
        if(!Files.exists(to)){
            Files.createDirectories(to);
        }
        // == 开始复制文件
        Files.walk(from).forEach(f -> {
            try{
                // === 解析得到文件复制过来后，应该生成的路径：先获取相对路径，然后和目标路径进行拼接
                Path q = to.resolve(from.relativize(f));
                // === 如果当前复制的文件是一个文件夹，则创建一个对应的文件夹目录
                if(Files.isDirectory(f)){
                    Files.createDirectories(q);
                }else{
                    // === 否则直接复制
                    Files.copy(f,q);
                }
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }

        });
    }

    // 获取文件后缀名（不带点）
    public static String getFileExtension(String fileName){
        if(fileName == null){
            return "";
        }
        // Just have blank or empty string.
        fileName = fileName.trim();
        if(fileName.isEmpty()){
            return "";
        }
        // split by '.'
        String[] strArray = fileName.split("\\.");
        if(strArray.length == 1){
            // The file is not a model like "xxx.extension", it is "xxx", so return empty string.
            return "";
        }else {
            // the extension will be the last separate.
            return strArray[strArray.length - 1].toLowerCase();
        }
    }
}
