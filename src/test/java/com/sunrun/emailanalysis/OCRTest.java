package com.sunrun.emailanalysis;

import com.sunrun.emailanalysis.service.OcrService;
import org.junit.Test;
public class OCRTest {
    @Test
    public void test() {
        System.out.println("OCR test");
        OcrService service = new OcrService();
        String text = service.ocrStarter("C:\\Users\\hongqun\\IdeaProjects\\email_analysis\\src\\test\\data\\images\\invoice2.jpg");
        System.out.println(text);
    }
}
