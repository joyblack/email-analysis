package com.sunrun.emailanalysis;

import org.junit.Test;
import org.thymeleaf.util.ArrayUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailTest {

    @Test
    public void showContent() throws IOException {
        // 保存测试
        Path path = Paths.get("content.html");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND))
        {
            writer.write("======== Email =========</br></br>");
            writer.write("======== textContent =========</br></br>");
            //writer.write(extractResult.getEmail().getTextContent() + "</br></br>");
            writer.write("======== htmlContent =========</br></br>");
            //writer.write(extractResult.getEmail().getHtmlContent() + "</br></br>");
        }
    }

    @Test
    public void getFromIP() throws MessagingException, FileNotFoundException {
        Properties properties = new Properties();
        Session session = Session.getInstance(properties,null);
        File file = new File("C:\\Users\\10160\\Desktop\\ea\\62测试邮件");
        for (File listFile : file.listFiles()) {
            System.out.println("====================================");
            System.out.println("Handler file: " + listFile.getAbsolutePath());
            MimeMessage mimeMessage = new MimeMessage(session, new FileInputStream(listFile));
            String[] header = mimeMessage.getHeader("X-Originating-IP");
            if(header != null){
                List<String> list = Arrays.asList(header);
                System.out.println(list);
            }else{
                System.out.println("header is null");
            }
        }
    }
}
