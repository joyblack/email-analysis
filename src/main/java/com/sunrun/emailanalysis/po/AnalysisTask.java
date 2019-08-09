package com.sunrun.emailanalysis.po;

import javax.persistence.Table;
import java.util.Date;

@Table(name="ea_analysis_task")
public class AnalysisTask {
    private Long taskId;

    private String taskName;

    private String taskStatus;

    private Long totalEmail;

    private Long caseId;

    private Date createTime;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}