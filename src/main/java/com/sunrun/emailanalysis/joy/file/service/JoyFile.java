package com.sunrun.emailanalysis.joy.file.service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public interface JoyFile {
    // store file to local.
    void storeFile(InputStream in, String targetFile) throws Exception;

    // list file.
    List<File> listFile(File dir, FileFilter filter, boolean rescue);

    // create dir
    boolean mkdir(String filePath);

    // Get file input stream
    FileInputStream getFileInputStream(String filePath);

    // Download File
    void download(String filePath, String fileName, HttpServletResponse response);

    // Download File
    void deleteDir(String filePath);
}
