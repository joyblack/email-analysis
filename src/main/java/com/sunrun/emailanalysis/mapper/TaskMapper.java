package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.po.Task;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TaskMapper extends Mapper<Task> {
    Integer updateTaskState(@Param("taskId") Long taskId, @Param("taskState") String taskState);

    Task getTaskByCaseIdAndType(@Param("caseId") Long caseId, @Param("type") String type);
}