package com.sunrun.emailanalysis.ea.extract;


import com.sunrun.emailanalysis.po.EmailCase;

import java.io.File;

public class ExtractFactory {
    // Default is used java mail to extract.
    public static Extract buildExtract(EmailCase emailCase, File emailFile){
          return new JavaMailExtract(emailCase, emailFile);
    }

    // Other type.
    public static Extract buildExtract(EmailCase emailCase, File emailFile, ExtractType extractType){
        switch (extractType) {
            case MIME4J: return new Mime4jExtract();
            case JAVAMAIL: return new JavaMailExtract(emailCase, emailFile);
            default: return new JavaMailExtract(emailCase, emailFile);
        }
    }

}
