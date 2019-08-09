package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="ea_statistic_attach")
public class StatisticAttach {
    @Id
    private Long statisticId;

    private Long caseId;

    private Long appearTime;

    private String extension;

    private Date caseUpdateTime;



    public Long getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(Long statisticId) {
        this.statisticId = statisticId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getCaseUpdateTime() {
        return caseUpdateTime;
    }

    public void setCaseUpdateTime(Date caseUpdateTime) {
        this.caseUpdateTime = caseUpdateTime;
    }

    public Long getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(Long appearTime) {
        this.appearTime = appearTime;
    }
}