package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.mapper.EmailAttachMapper;
import com.sunrun.emailanalysis.mapper.EmailCaseMapper;
import com.sunrun.emailanalysis.mapper.SysConfigMapper;
import com.sunrun.emailanalysis.task.info.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AnalysisTaskService {
    private static Logger log = LoggerFactory.getLogger(AnalysisTaskService.class);

    @Autowired
    private EmailAttachMapper emailAttachMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private HashMap<Long, TaskInfo> analysisTask;

    public HashMap<Long, TaskInfo> getTaskList() {
        try {
            return analysisTask;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public TaskInfo getTask(Long taskId) {
        try {
            return analysisTask.get(taskId);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public void deleteTask(Long taskId) {
        try {
            analysisTask.remove(taskId);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public void clearTask() {
        try {
            analysisTask.clear();
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }
}
