package com.sunrun.emailanalysis.data.request.statistic;

public class EmailTotalData {
    private Long caseId;

    public EmailTotalData() {
    }

    public EmailTotalData(Long caseId) {
        this.caseId = caseId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }
}
