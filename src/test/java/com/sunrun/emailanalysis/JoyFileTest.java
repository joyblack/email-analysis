package com.sunrun.emailanalysis;

import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.dictionary.file.FileTypeExtension;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.file.factory.JoyFileFactory;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class JoyFileTest {

    @Test
    public void test(){
        // get now filesystem ea file type.
        JoyFile JoyFile = JoyFileFactory.factory(FileProtocol.LOCAL);

        // get input email list.
        List<File> emailFiles = JoyFile.listFile(new File("D:\\email\\email\\1.eml"),
                f -> f.getName().endsWith(FileTypeExtension.EMAIL_1) || f.getName().endsWith(FileTypeExtension.EMAIL_2),
                true);

        System.out.println(emailFiles);
    }

    @Test
    public void test1(){
        String path = "a/b/c/d/e";
        boolean result = new File(path).mkdirs();
        System.out.println(result);
    }
}
