package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="ea_statistic_relation")
public class StatisticRelation {
    @Id
    private Long statisticId;

    private Long caseId;

    private String address;

    private String sendType;

    private String nickName;

    private Long appearTime;

    private Date caseUpdateTime;

    private String domainName;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(Long appearTime) {
        this.appearTime = appearTime;
    }

    public Date getCaseUpdateTime() {
        return caseUpdateTime;
    }

    public void setCaseUpdateTime(Date caseUpdateTime) {
        this.caseUpdateTime = caseUpdateTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}