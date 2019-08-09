package com.sunrun.emailanalysis.ea.recognition.index;

import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.ea.extract.ExtractResult;
import com.sunrun.emailanalysis.ea.recognition.dictionary.EmailIndexDictionary;
import com.sunrun.emailanalysis.po.Email;
import com.sunrun.emailanalysis.po.EmailAttach;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.tool.common.DateUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Use apache lucene to index.
 */
public class DefaultEAIndex implements EAIndex {

    private static Logger log = LoggerFactory.getLogger(DefaultEAIndex.class);

    // Field analyzer.
    private static PerFieldAnalyzerWrapper analyzer;

    // Index writer.
    private IndexWriter indexWriter;

    static {
        // Create per field analyzer wrapper.
        Map<String, Analyzer> map = new HashMap<>();
        analyzer  = new PerFieldAnalyzerWrapper(new SmartChineseAnalyzer(),map);
    }

    DefaultEAIndex(String indexPath, FileProtocol protocol) throws IOException {
        // == index
        // 1.path: config.indexPath
        // 2.index field: please input.
        IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
        iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        indexWriter = new IndexWriter(FSDirectory.open(Paths.get(indexPath)),iwConfig);
    }

    public void close(){
        try{
            indexWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            log.error("Index close error: {}", e.getMessage());
        }
    }

    private void indexCommon(ExtractResult extractResult, Document doc){
        // Email basic info
        Email email = extractResult.getEmail();
        doc.add(new StoredField(EmailIndexDictionary.CASE_ID, email.getCaseId().toString()));
        doc.add(new StoredField(EmailIndexDictionary.EMAIL_ID, email.getEmailId().toString()));
        // just store
        doc.add(new StoredField(EmailIndexDictionary.CREATE_TIME, DateUtil.format(email.getCreateTime())));
        doc.add(new StringField(EmailIndexDictionary.SEND_TIME, DateUtil.format(email.getSendTime()), Field.Store.YES));
        doc.add(new TextField(EmailIndexDictionary.FROM_NAME, email.getFromName() , Field.Store.YES));
        doc.add(new TextField(EmailIndexDictionary.FROM_ADDRESS, email.getFromAddress(), Field.Store.YES));
        doc.add(new TextField(EmailIndexDictionary.TO_NAME, email.getToName() , Field.Store.YES));

        // to address
        doc.add(new TextField(EmailIndexDictionary.TO_ADDRESS, email.getToAddress(), Field.Store.YES));

        // subject
        if(email.getEmailSubject() != null){
            doc.add(new TextField(EmailIndexDictionary.SUBJECT, email.getEmailSubject(), Field.Store.YES));
        }

        // from ip
        if(email.getFromIp() != null){
            doc.add(new StringField(EmailIndexDictionary.FROM_IP, email.getFromIp(), Field.Store.YES));
        }

        // new file name
        doc.add(new StoredField(EmailIndexDictionary.NEW_FILE_NAME, email.getNewFileName()));

        // old file name
        doc.add(new StoredField(EmailIndexDictionary.OLD_FILE_NAME, email.getOldFileName()));
    }


    @Override
    public void indexAttach(ExtractResult extractResult, EmailAttach attach, String attachContent) {
        try {
            log.info("开始写入附件索引...");
            // Email basic info
            Email email = extractResult.getEmail();
            Document doc = new Document();
            indexCommon(extractResult,doc);
            doc.add(new TextField(EmailIndexDictionary.ATTACH_ID, attach.getAttachId().toString(), Field.Store.YES));
            if(attachContent != null){
                doc.add(new TextField(EmailIndexDictionary.ATTACH_CONTENT, attachContent, Field.Store.YES));
            }
            indexWriter.addDocument(doc);
            indexWriter.commit();
            log.info("附件索引提交完毕...");

        } catch (Exception e) {
            log.error("[black] Index Error: {}",e.getMessage());
        }
        log.info("End index to attach.");
    }

    @Override
    public void indexEmail(ExtractResult extractResult) {
        try {
            log.info("开始写入邮件索引...");
            // Email basic info
            Email email = extractResult.getEmail();
            // Lucene doc object.
            Document doc = new Document();
            indexCommon(extractResult, doc);
            // 如果是针对内容的话，将html的文本部分以及文本部分进行索引
            if(email.getTextContent() != null){
                doc.add(new TextField(EmailIndexDictionary.TEXT_CONTENT, email.getTextContent(), Field.Store.YES));
            }
            // 是否需要存储HTML_CONTENT呢？
            indexWriter.addDocument(doc);
            indexWriter.commit();
            log.info("邮件索引提交完毕...");
        } catch (Exception e) {
            System.out.println("[black] Index Error: " + e.getMessage());
        }
    }
}

