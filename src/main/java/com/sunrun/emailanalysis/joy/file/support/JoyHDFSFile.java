package com.sunrun.emailanalysis.joy.file.support;

import com.sunrun.emailanalysis.joy.file.service.JoyFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class JoyHDFSFile implements JoyFile {
    @Override
    public void storeFile(InputStream in, String targetFile) throws Exception {

    }

    @Override
    public boolean mkdir(String filePath) {
        return false;
    }

    @Override
    public List<File> listFile(File dir, FileFilter filter, boolean rescue) {
        return null;
    }

    // 获取文件的输入流
    @Override
    public FileInputStream getFileInputStream(String filePath){
        return null;
    }

    @Override
    public void download(String filePath, String fileName, HttpServletResponse response) {

    }

    // 删除指定文件夹
    @Override
    public void deleteDir(String filePath) {

    }
}
