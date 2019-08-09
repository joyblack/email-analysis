package com.sunrun.emailanalysis.ea.extract;

import com.sunrun.emailanalysis.dictionary.database.CommonDictionary;
import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.file.factory.JoyFileFactory;
import com.sunrun.emailanalysis.parser.html.EAHtmlParser;
import com.sunrun.emailanalysis.po.Email;
import com.sunrun.emailanalysis.po.EmailAttach;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.po.EmailRelation;
import com.sunrun.emailanalysis.tool.JoyFileOperateTool;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * 使用JavaMail作为分析工具来解析邮件
 */
public class JavaMailExtract implements Extract{

    private static Logger log = LoggerFactory.getLogger(JavaMailExtract.class);

    // Extract id ,it will be set the email db id.
    private Long extractId;

    // Main content buffer.
    private StringBuffer textContentBuffer;

    // Html text buffer.
    private StringBuffer htmlContentBuffer;

    // email
    private Email email;

    // Handler email configuration.
    private EmailCase emailCase;

    private JoyFile joyFile;

    private File emailFile;

    private MimeMessage mimeMessage;

    private List<EmailAttach> attaches;

    private List<EmailRelation> relations;

    private String attachPath;

    JavaMailExtract(EmailCase emailCase, File emailFile){
        // Copy, it will be used to thread compute so we need copy.
        this.emailCase = emailCase;
        // File info
        this.emailFile = emailFile;

        initializer();
    }

    /**
     * initialize some tool.
     */
    private void initializer(){
        // extract id
        extractId = JoyUUID.getUUId();

        // email
        email = new Email();

        // relations
        relations = new ArrayList<>();

        // JoyFile
        FileProtocol protocol = FileProtocol.valueOf(emailCase.getProtocol().toUpperCase());
        joyFile = JoyFileFactory.factory(protocol);

        // Store dir will be created and path set be 'config.getAttachPath()/emailId'.
        attachPath = emailCase.getAttachPath() + File.separator + this.extractId;
        joyFile.mkdir(attachPath);

        // attaches
        attaches = new ArrayList<>();

        // buffer
        textContentBuffer = new StringBuffer();
        htmlContentBuffer = new StringBuffer();


    }

    @Override
    public ExtractResult extract() throws Exception{
        // Set mime Message
        FileInputStream inputStream = new FileInputStream(emailFile);
        Properties properties = new Properties();
        Session session = Session.getInstance(properties,null);
        mimeMessage = new MimeMessage(session, inputStream);

        // Extract basic info.
        extractEmailBasicInfo();

        // Extract relation info.
        extractRelation();

        // Extract Attaches.
        extractContent();

        // 将邮件存储到本地
        storeEmailFile();

        // 将三部分的内容填充到email信息中.
        setEmailOtherInfo(email, relations);

        return new ExtractResult(email,
                relations,
                attaches);
    }

    // 设置三种内容类型
    // 1.textContent: 文本部分，富文本
    // 2.htmlContent：html部分
    // 3.htmlTextContent：从html中提取的信息
    // 4.toName、toAddress.
    private void setEmailOtherInfo(Email email, List<EmailRelation> relations){
        // Traditional to simple.
        //return ZHConverter.convert(textContentBuffer.toString(),ZHConverter.SIMPLIFIED);
        email.setTextContent(textContentBuffer.toString());
        email.setHtmlContent(htmlContentBuffer.toString());
        // email text content should equal extract form html content, so delete the param 'htmlTextContent'
        //email.setHtmlTextContent(EAHtmlParser.getTextFromHtmlContent(htmlContent));

        // toName & toAddress: Get the first relation's be the toName,toAddress
        if(relations.size() > 0){
            email.setToName(relations.get(0).getToName());
            email.setToAddress(relations.get(0).getToAddress());
        }else{
            email.setToName(ExtractDictionary.UNKNOWN);
            email.setToAddress(ExtractDictionary.UNKNOWN);
        }
    }

    // Store the original email file to our target path: it maybe other distribute file system.
    private void storeEmailFile() throws Exception{
        log.info("Store old email file to target path...");
        String fileName = extractId + ".eml";
        String storePath = emailCase.getEmailPath() + File.separator  + fileName;
        joyFile.storeFile(new FileInputStream(emailFile),storePath);
        // Set the email information
        email.setOldFileName(emailFile.getName());
        email.setNewFileName(fileName);
        email.setFileName(fileName);
        email.setStorePath(storePath);
        email.setFileProtocol(emailCase.getProtocol());
        email.setFileSize(emailFile.length());
    }


    // Resolve email basic information.
    private void extractEmailBasicInfo() throws Exception{
        log.info("Extract email basic information...");
        // Set email id be extract id.
        email.setEmailId(extractId);
        // send user address
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        email.setFromAddress(address[0].getAddress());

        // send user name
        email.setFromName(address[0].getPersonal());

        // email subject
        email.setEmailSubject(getSubject());

        // need reply
        email.setNeedReply(getReplySign());

        // is read
        email.setIsNew(isNew());

        // send time
        email.setSendTime(getSendDate());

        // from ip
        email.setFromIp(getFromIP());

    }

    private void extractRelation() throws Exception{
        log.info("Extract email send relations...");
        // Receive relation(to,cc,bcc).
        relations.addAll(getMailToAddress(Message.RecipientType.TO));
        relations.addAll(getMailToAddress(Message.RecipientType.CC));
        relations.addAll(getMailToAddress(Message.RecipientType.BCC));
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，
     * 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     */
    private void extractContent() throws Exception {
        Object o = mimeMessage.getContent();
        if(o instanceof Multipart){
            Multipart multipart = (Multipart) o;
            extractMultipart(multipart);
        }else if (o instanceof Part) {
            Part part = (Part) o;
            extractPart(part);
        } else {
            log.info("[black] 文件内容既不是Part，也不是MultiPart...");
        }



    }


    /**
     * 接卸包裹（含所有邮件内容(包裹 + 正文+ 附件)
     * @param multipart multipart
     * @throws Exception exception
     */
    private void  extractMultipart(Multipart multipart) throws Exception {
        // log.info("邮件共有" + multipart.getCount() + "部分组成");
        // 依次处理各个部分
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            // log.info("处理第" + j + "部分");
            // 解包, 取出 MultiPart的各个部分,
            Part part = multipart.getBodyPart(j);
            // 每部分可能是邮件内容,也可能是另一个小包裹(Multipart)：
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是正文Content-Type: multipart/alternative
            if (part.getContent() instanceof Multipart) {
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                // 递归迭代
                extractMultipart(p);
            } else {
                extractPart(part);
            }
        }
    }

    /**
     * 解析part
     * @param part part对象
     */
    private void extractPart(Part part) throws Exception {
        // has attach?
        String disposition = part.getDisposition();
        // This is a attach,store in configured path.
        if (disposition != null) {
            handlerAttach(part);
        } else {
            String contentType = part.getContentType();
            // text content, add to buffer.
            if (contentType.startsWith("text/plain")) {
                // log.info("文本内容：" + part.getContent());
                textContentBuffer.append(part.getContent());
            }else if(contentType.startsWith("text/html")){
                // log.info("html内容：" + part.getContent());
                htmlContentBuffer.append(part.getContent());
            }else{
                log.info("当前还未添加的分支类型，放弃处理。mime type:" + contentType + "，内容为" + part.getContent());
                //handlerAttach(part);
            }
        }
    }

    /**
     * 获得邮件发送日期
     */
    private Date getSendDate() throws Exception {
        return mimeMessage.getSentDate();
    }

    /**
     * 判断此邮件是否已读
     */
    private int isNew() throws MessagingException {
        int isNew = 0;
        Flags flags = ((Message) mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (Flags.Flag aFlag : flag) {
            if (aFlag == Flags.Flag.SEEN) {
                isNew = 1;
                break;
            }
        }
        return isNew;
    }



    // Extract email Subject
    private String getSubject(){
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return subject;
    }

    // Get the email need reply info.
    private int getReplySign() throws MessagingException {
        int replySign = 0;
        String needReply[] = mimeMessage.getHeader("Disposition-Notification-To");
        if (needReply != null) {
            replySign = 1;
        }
        return replySign;
    }

    // Get the receiver,it include "to" "cc" "bcc"
    private List<EmailRelation> getMailToAddress(Message.RecipientType type) throws Exception {
        List<EmailRelation> result = new ArrayList<>();
        InternetAddress[] addresses;
        try{
            addresses = (InternetAddress[]) mimeMessage.getRecipients(type);
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
        if(addresses == null){
            return result;
        }
        for (InternetAddress address : addresses){
            EmailRelation relation = new EmailRelation();
            // set to name.
            String toAddress = address.getAddress();
            if (toAddress == null)
                toAddress = "";
            else {
                toAddress = MimeUtility.decodeText(toAddress);
            }
            relation.setToAddress(toAddress);

            // set to address.
            String toName = address.getPersonal();
            if (toName == null)
                toName = "";
            else {
                toName = MimeUtility.decodeText(toName);
            }
            relation.setToName(toName);
            // set type:TO CC BCC
            relation.setSendType(type.toString().toUpperCase());
            // set other redundancy info.
            relation.setFromName(email.getFromName());
            relation.setFromAddress(email.getFromAddress());
            relation.setEmailId(email.getEmailId());
            relation.setRelationId(JoyUUID.getUUId());
            relation.setSendTime(email.getSendTime());
            relation.setCaseId(emailCase.getCaseId());
            result.add(relation);
        }
        return result;
    }


    // Handler attachment in config assign file system and remember attach info.
    private void handlerAttach(Part part) throws Exception {
        log.info("发现附件，开始存储...");
        String fileName = part.getFileName();
        if (StringUtils.isEmpty(fileName)) {
            log.info("=== The attachment file name is null.");
            fileName = "random_" + UUID.randomUUID();
        }
        // MimeUtility.decodeText is used to solve the file name messy code problem.
        fileName = MimeUtility.decodeText(fileName);

        // Store attachment.
        log.info("附件文件名为：" + fileName);
        String storePath = attachPath + File.separator + fileName;
        joyFile.storeFile(part.getInputStream(), storePath);

        // Add attach info in attach list.
        EmailAttach attach = new EmailAttach();
        attach.setAttachId(JoyUUID.getUUId());
        attach.setFileName(fileName);
        attach.setStorePath(storePath);
        attach.setEmailId(extractId);
        attach.setFileMd5(CommonDictionary.TEMPORARY_NOT_HANDLER);
        attach.setFileProtocol(emailCase.getProtocol());
        attach.setFileSize((long)part.getSize());
        attach.setFileType(JoyFileOperateTool.getFileExtension(fileName));
        attach.setCaseId(emailCase.getCaseId());
        attaches.add(attach);
    }

    private String getFromIP(){
        String[] header;
        try {
            header = mimeMessage.getHeader("X-Originating-IP");
            if(header != null && header.length > 0){

                return header[0].replace("[","").replace("]","");
            }
        } catch (Exception e) {
            log.info("Extract from user IP error:" + e.getMessage());
        }
        return "";
    }
}
