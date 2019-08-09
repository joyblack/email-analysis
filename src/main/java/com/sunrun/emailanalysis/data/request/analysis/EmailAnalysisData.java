package com.sunrun.emailanalysis.data.request.analysis;

import com.sunrun.emailanalysis.ea.config.AnalysisConfig;

import java.io.Serializable;

public class EmailAnalysisData implements Serializable {
    private String caseName;
    private String protocol;
    private String filePath;
    private Integer caseType;
    private Long taskId;

    // analysis config.
    private AnalysisConfig analysisConfig;

    public EmailAnalysisData() {
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String email) {
        this.protocol = email;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getCaseType() {
        return caseType;
    }

    public void setCaseType(Integer caseType) {
        this.caseType = caseType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public AnalysisConfig getAnalysisConfig() {
        return analysisConfig;
    }

    public void setAnalysisConfig(AnalysisConfig analysisConfig) {
        this.analysisConfig = analysisConfig;
    }

    @Override
    public String toString() {
        return "EmailAnalysisData{" +
                "caseName='" + caseName + '\'' +
                ", protocol='" + protocol + '\'' +
                ", filePath='" + filePath + '\'' +
                ", caseType=" + caseType +
                ", taskId=" + taskId +
                ", analysisConfig=" + analysisConfig +
                '}';
    }
}
