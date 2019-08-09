package com.sunrun.emailanalysis.data.request.analysis;

public class MonitorData {
    private Long taskId;

    public MonitorData() {
    }

    public MonitorData(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
