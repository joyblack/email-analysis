package com.sunrun.emailanalysis.parser.html;

import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EAHtmlParser {

    private static Logger log = LoggerFactory.getLogger(EAHtmlParser.class);

    // By url to parser html content and get text info.
    public static String getTextFromUrl(String url) {
        try{
            StringBean sb = new StringBean();
            // Set the 'include links' state.
            sb.setLinks(false);
            // Set the 'replace non breaking spaces' state.
            sb.setReplaceNonBreakingSpaces(true);
            // Set the current 'collapse whitespace' state.
            sb.setCollapse(true);
            // Set parser url
            sb.setURL(url);
            return sb.getStrings();
        }catch (Exception e){
            log.error("Parser error: {}", e.getMessage());
        }
        return null;
    }

    // By url to parser html content and get text info.
    public static String getTextFromHtmlContent(String htmlContent) {
        try{
            Parser parser = new Parser(htmlContent);
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            return visitor.getExtractedText();
        }catch (Exception e){
            log.error("Parser error: {}", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String content = getTextFromHtmlContent( "\r\n\r\n\r\n\r\nBLOCKQUOTE {\r\n\tMARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; MARGIN-LEFT: 2em\r\n}\r\nOL {\r\n\tMARGIN-TOP: 0px; MARGIN-BOTTOM: 0px\r\n}\r\nUL {\r\n\tMARGIN-TOP: 0px; MARGIN-BOTTOM: 0px\r\n}\r\nP {\r\n\tMARGIN-TOP: 0px; MARGIN-BOTTOM: 0px\r\n}\r\nBODY {\r\n\tFONT-SIZE: 10.5pt; COLOR: #000000; LINE-HEIGHT: 1.5; FONT-FAMILY: 宋体\r\n}\r\n\r\n\r\n\r\n\r\n \r\n \r\n \r\n \r\n请查收，谢！\r\n \r\n \r\n\r\n\r\nhkjontim\r\n"
    );
        System.out.println(content);

    }
}
