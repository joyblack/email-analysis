package com.sunrun.emailanalysis.data.request.email;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class SearchEmailByAccountData {
    // Case id
    private Long caseId;

    // Search key word
    private String account;

    // isExtract
    private Integer isExact;

    // Pagination information.
    private Pagination pagination;

    public SearchEmailByAccountData(){}



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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
                ", account='" + account + '\'' +
                ", isExact=" + isExact +
                ", pagination=" + pagination +
                '}';
    }
}
