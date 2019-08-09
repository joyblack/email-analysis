package com.sunrun.emailanalysis.data.request.domain;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class DomainListData {
    // Case id
    private Long caseId;

    // Search key word
    private String keyword;

    // Pagination information.
    private Pagination pagination;

    public DomainListData(){}

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
