package com.sunrun.emailanalysis.data.request.statistic;

import java.util.List;

public class AttachTypeData {
    private Long caseId;
    private List<String> fileType;
    private Integer statisticType;
    private Integer include;

    public AttachTypeData() {
    }

    public AttachTypeData(Long caseId, List<String> fileType, Integer statisticType, Integer include) {
        this.caseId = caseId;
        this.fileType = fileType;
        this.statisticType = statisticType;
        this.include = include;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public List<String> getFileType() {
        return fileType;
    }

    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }

    public Integer getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(Integer statisticType) {
        this.statisticType = statisticType;
    }

    public Integer getInclude() {
        return include;
    }

    public void setInclude(Integer include) {
        this.include = include;
    }

    @Override
    public String toString() {
        return "AttachTypeData{" +
                "caseId=" + caseId +
                ", fileType=" + fileType +
                ", statisticType=" + statisticType +
                ", include=" + include +
                '}';
    }
}
