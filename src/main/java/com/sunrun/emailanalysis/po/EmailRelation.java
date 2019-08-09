package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="ea_email_relation")
public class EmailRelation {
    @Id
    private Long relationId;

    private Long caseId;

    private Long emailId;

    private String fromName;

    private String fromAddress;

    private String toName;

    private String toAddress;

    private Date sendTime;

    private String sendType;

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    @Override
    public String toString() {
        return "EmailRelation{" +
                "relationId=" + relationId +
                ", caseId=" + caseId +
                ", emailId=" + emailId +
                ", fromName='" + fromName + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", toName='" + toName + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", sendTime=" + sendTime +
                ", sendType='" + sendType + '\'' +
                '}';
    }
}