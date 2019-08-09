package com.sunrun.emailanalysis.data.request.common;

public class KeywordData {
    private String keyword;

    private Pagination pagination;

    public KeywordData(){}

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
        return "KeywordData{" +
                "keyword='" + keyword + '\'' +
                ", pagination=" + pagination +
                '}';
    }
}
