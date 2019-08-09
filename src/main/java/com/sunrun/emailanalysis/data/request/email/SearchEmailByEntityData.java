package com.sunrun.emailanalysis.data.request.email;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class SearchEmailByEntityData {
    // Case id
    private Long caseId;

    // Search key word
    private String entityValue;

    // isExtract
    private Integer isExact;

    // Pagination information.
    private Pagination pagination;

    public SearchEmailByEntityData(){}

    public SearchEmailByEntityData(Long caseId, String entityValue, Integer isExact, Pagination pagination){
        this.caseId = caseId;
        this.entityValue = entityValue;
        this.pagination = pagination;
        this.isExact = isExact;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }


    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(String entityValue) {
        this.entityValue = entityValue;
    }

    public Integer getIsExact() {
        return isExact;
    }

    public void setIsExact(Integer isExact) {
        this.isExact = isExact;
    }

    @Override
    public String toString() {
        return "SearchEmailByEntityData{" +
                "caseId=" + caseId +
                ", entityValue='" + entityValue + '\'' +
                ", isExact=" + isExact +
                ", pagination=" + pagination +
                '}';
    }
}
