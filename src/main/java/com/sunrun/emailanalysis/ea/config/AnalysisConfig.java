package com.sunrun.emailanalysis.ea.config;

public class AnalysisConfig {
    // 是否开启实体识别
    private Boolean enableEntity;

    // 是否开启邮件告警
    private Boolean enableWarningAccount;

    public AnalysisConfig() {
    }

    public AnalysisConfig(Boolean enableEntity) {
        this.enableEntity = enableEntity;
    }

    public Boolean getEnableEntity() {
        return enableEntity;
    }

    public void setEnableEntity(Boolean enableEntity) {
        this.enableEntity = enableEntity;
    }

    public Boolean getEnableWarningAccount() {
        return enableWarningAccount;
    }

    public void setEnableWarningAccount(Boolean enableWarningAccount) {
        this.enableWarningAccount = enableWarningAccount;
    }

    @Override
    public String toString() {
        return "AnalysisConfig{" +
                "enableEntity=" + enableEntity +
                ", enableWarningAccount=" + enableWarningAccount +
                '}';
    }
}
