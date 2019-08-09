package com.sunrun.emailanalysis.data.request.cases;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class CaseListData {
    // search key word
    private String keyword;

    // pagination information.
    private Pagination pagination;

    public CaseListData(){}

    public CaseListData(String entityType, String keyword, Pagination pagination) {
        this.keyword = keyword;
        this.pagination = pagination;
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


    @Override
    public String toString() {
        return "EntityListData{" +
                "keyword='" + keyword + '\'' +
                ", pagination=" + pagination +
                '}';
    }
}
