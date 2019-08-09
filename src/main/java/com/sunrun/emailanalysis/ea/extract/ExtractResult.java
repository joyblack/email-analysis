package com.sunrun.emailanalysis.ea.extract;

import com.sunrun.emailanalysis.po.Email;
import com.sunrun.emailanalysis.po.EmailAttach;
import com.sunrun.emailanalysis.po.EmailRelation;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 提取邮件信息的结果，包含几个部分
 * 1. 邮件基本信息：标题，收件人...
 * 2. 邮件正文信息
 * 3. 邮件附件信息
 */
public class ExtractResult {


    // Email basic information.
    private Email email;

    // Attachment information.
    private List<EmailAttach> attaches;

    // Email relation information.
    private List<EmailRelation> relations;
    
    private ExtractResult() {
    }

    public ExtractResult( Email email, List<EmailRelation> relations, List<EmailAttach> attaches) {
        this.email = email;
        this.attaches = attaches;
        this.relations = relations;
    }

    public Email getEmail() {
        return email;
    }

    public List<EmailAttach> getAttaches() {
        return attaches;
    }

    public List<EmailRelation> getRelations() {
        return relations;
    }
}
