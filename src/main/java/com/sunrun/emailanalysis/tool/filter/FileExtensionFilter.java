package com.sunrun.emailanalysis.tool.filter;

import com.sunrun.emailanalysis.dictionary.file.FileTypeExtension;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(FileTypeExtension.EMAIL_1)
                || pathname.getName().toLowerCase().endsWith(FileTypeExtension.EMAIL_2);
    }
}
