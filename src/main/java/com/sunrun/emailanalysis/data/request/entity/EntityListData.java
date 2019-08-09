package com.sunrun.emailanalysis.data.request.entity;

import com.sunrun.emailanalysis.data.request.common.Pagination;
import com.sunrun.emailanalysis.po.EmailEntity;

import java.util.List;

public class EntityListData {
    // Entity type name.
    private List<EmailEntity> entities;
    // Case id.
    private Long caseId;
    // Email id.
    private Long emailId;
    // pagination information.
    private Pagination pagination;

    public EntityListData(){}

    public EntityListData(List<EmailEntity> entities, Pagination pagination) {
        this.entities = entities;
        this.pagination = pagination;
    }


    public List<EmailEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<EmailEntity> entities) {
        this.entities = entities;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "EntityListData{" +
                "entities=" + entities +
                ", caseId=" + caseId +
                ", emailId=" + emailId +
                ", pagination=" + pagination +
                '}';
    }
}
