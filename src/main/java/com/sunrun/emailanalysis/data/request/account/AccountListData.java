package com.sunrun.emailanalysis.data.request.account;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class AccountListData {
    // Case id
    private Long caseId;

    // Search key word
    private String keyword;

    // domainName
    private String domainName;

    // isExtract
    private Integer isExact;

    // Pagination information.
    private Pagination pagination;

    public AccountListData(){}

    public AccountListData(Long caseId, String domainName, String keyword, Integer isExact, Pagination pagination){
        this.caseId = caseId;
        this.keyword = keyword;
        this.pagination = pagination;
        this.isExact = isExact;
        this.domainName = domainName;
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



    public Integer getIsExact() {
        return isExact;
    }

    public void setIsExact(Integer isExact) {
        this.isExact = isExact;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "AccountListData{" +
                "caseId=" + caseId +
                ", keyword='" + keyword + '\'' +
                ", domainName='" + domainName + '\'' +
                ", isExact=" + isExact +
                ", pagination=" + pagination +
                '}';
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
