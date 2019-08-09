package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="ea_email_case")
public class EmailCase {

    @Id
    private Long caseId;

    private String caseName;

    private Integer caseType;

    private String caseCode;

    private Integer state;

    private Date createTime;

    private Date editTime;

    private Long createId;

    private Long totalEmail;

    private String remark;

    private String indexPath;

    private String emailPath;

    private String attachPath;

    private String protocol;

    public Long getTotalEmail() {
        return totalEmail;
    }

    public void setTotalEmail(Long totalEmail) {
        this.totalEmail = totalEmail;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public Integer getCaseType() {
        return caseType;
    }

    public void setCaseType(Integer caseType) {
        this.caseType = caseType;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public String getEmailPath() {
        return emailPath;
    }

    public void setEmailPath(String emailPath) {
        this.emailPath = emailPath;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}