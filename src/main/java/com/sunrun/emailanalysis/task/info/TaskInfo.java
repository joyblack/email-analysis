package com.sunrun.emailanalysis.task.info;

public class TaskInfo {
    private Long taskId;
    private TaskState state;
    private Long total;

    public TaskInfo() {
    }
    public TaskInfo(Long taskId, TaskState state, Long total) {
        this.taskId = taskId;
        this.state = state;
        this.total = total;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
