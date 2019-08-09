package com.sunrun.emailanalysis.data.request.task;

public class TaskInfoData {
    private Long taskId;

    public TaskInfoData() {
    }

    public TaskInfoData(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
