package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ea_email_entity")
public class EmailEntity {
    @Id
    private Long id;

    private Long emailId;

    private Long attachId;

    private String entityValue;

    private String entityType;

    private Integer appearPosition;

    private Long caseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(String entityValue) {
        this.entityValue = entityValue;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getAppearPosition() {
        return appearPosition;
    }

    public void setAppearPosition(Integer appearPosition) {
        this.appearPosition = appearPosition;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    @Override
    public String toString() {
        return "EmailEntity{" +
                "id=" + id +
                ", emailId=" + emailId +
                ", attachId=" + attachId +
                ", entityValue='" + entityValue + '\'' +
                ", entityType='" + entityType + '\'' +
                ", appearPosition=" + appearPosition +
                ", caseId=" + caseId +
                '}';
    }
}