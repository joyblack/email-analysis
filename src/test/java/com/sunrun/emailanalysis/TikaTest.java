package com.sunrun.emailanalysis;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TikaTest {

    public static String readPDF(String fileName){
        InputStream inputStream = null;
        String content = "";
        try {
            inputStream = new FileInputStream(fileName);
            // 创建一个自动解析器
            AutoDetectParser parser = new AutoDetectParser();
            // 使用-1表示不对文件内容的大小进行限制
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metaData = new Metadata();
            parser.parse(inputStream, handler, metaData, new ParseContext());
            //System.out.println(metaData);
            // 调用Handler对象的toString获取正文内容
            content = handler.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception e){
                    System.out.println("Error Closing input stream.");
                }
            }
        }
        return content;
    }

    @Test
    public void test1(){
        // pdf
        //System.out.println(readPDF("test/一种基于情感词典和朴素贝叶斯的中文文本情感分类方法.pdf"));
        // MD
        //System.out.println(readPDF("C:\\Users\\10160\\Desktop\\机器学习\\语料库\\readme.md"));
        // doc
        //System.out.println(readPDF("C:\\Users\\10160\\Desktop\\机器学习\\知识库\\spark\\【尚硅谷】Spark01 基础解析.docx"));
        // ppt
        System.out.println(readPDF("C:\\Users\\10160\\Desktop\\机器学习\\知识库\\spark\\【尚硅谷】决策树.pptx"));
        // excel
        System.out.println(readPDF("C:\\Users\\10160\\Desktop\\机器学习\\知识库\\spark\\【尚硅谷】决策树.pptx"));

    }

    @Test
    public void test(){
        File file = new File("test/一种基于情感词典和朴素贝叶斯的中文文本情感分类方法.pdf");
        Parser parser = new AutoDetectParser();

        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY,file.getName());

        BodyContentHandler handler = new BodyContentHandler();
        ParseContext parseContext = new ParseContext();

        parseContext.set(Parser.class,parser);

        try {
            parser.parse(new FileInputStream(file),handler,metadata,parseContext);

            for (String name : metadata.names()) {
                System.out.println("==============");
                System.out.println("name: " + name);
                System.out.println(metadata.get(name));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
