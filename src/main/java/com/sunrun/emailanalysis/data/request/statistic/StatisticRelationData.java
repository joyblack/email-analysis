package com.sunrun.emailanalysis.data.request.statistic;

import com.sunrun.emailanalysis.data.request.common.Pagination;

public class StatisticRelationData {
    private Long caseId;
    private String keyword;
    private Integer isExact;
    private Pagination pagination;
    private String sendType;

    public StatisticRelationData() {
    }

    public StatisticRelationData(Long caseId, String keyword, String sendType, Integer isExact,  Pagination pagination) {
        this.caseId = caseId;
        this.keyword = keyword;
        this.isExact = isExact;
        this.pagination = pagination;
        this.sendType = sendType;
    }

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

    public Integer getIsExact() {
        return isExact;
    }

    public void setIsExact(Integer isExact) {
        this.isExact = isExact;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @Override
    public String toString() {
        return "RelationData{" +
                "caseId=" + caseId +
                "sendType=" + sendType +
                ", keyword='" + keyword + '\'' +
                ", isExact=" + isExact +
                ", pagination=" + pagination +
                '}';
    }
}
