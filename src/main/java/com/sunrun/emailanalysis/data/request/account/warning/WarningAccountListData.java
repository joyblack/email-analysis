package com.sunrun.emailanalysis.data.request.account.warning;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class WarningAccountListData {
    // Search key word
    private String keyword;
    // Pagination information.
    private Pagination pagination;

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
        return "WarningAccountListData{" +
                "keyword='" + keyword + '\'' +
                ", pagination=" + pagination +
                '}';
    }
}
