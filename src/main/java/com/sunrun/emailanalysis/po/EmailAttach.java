package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ea_email_attach")
public class EmailAttach {

    @Id
    private Long attachId;

    private Long emailId;

    private Long caseId;

    private String fileName;

    private String storePath;

    private Long fileSize;

    private String fileType;

    private String fileMd5;

    private String fileProtocol;

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getFileProtocol() {
        return fileProtocol;
    }

    public void setFileProtocol(String fileProtocol) {
        this.fileProtocol = fileProtocol;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    @Override
    public String toString() {
        return "EmailAttach{" +
                "attachId=" + attachId +
                ", emailId=" + emailId +
                ", caseId=" + caseId +
                ", fileName='" + fileName + '\'' +
                ", storePath='" + storePath + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                ", fileProtocol='" + fileProtocol + '\'' +
                '}';
    }
}