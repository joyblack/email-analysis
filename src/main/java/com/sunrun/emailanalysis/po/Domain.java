package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ea_domain")
public class Domain {
    @Id
    private Long domainId;

    private String domainName;

    private Long caseId;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }
}