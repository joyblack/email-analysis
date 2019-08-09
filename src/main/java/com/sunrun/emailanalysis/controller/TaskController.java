package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.task.TaskInfoData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.AnalysisTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequestMapping("task")
@Controller
public class TaskController {
    private static Logger log = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private AnalysisTaskService analysisTaskService;

    @Autowired
    private EADataHandler dataHandler;

    @RequestMapping("getTaskList")
    @ResponseBody
    public EAResult getTaskList(){
        EAResult result = null;
        try{
            log.info("After handler input data is : null");
            result = EAResult.buildSuccessResult(analysisTaskService.getTaskList());
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("deleteTask")
    @ResponseBody
    public EAResult deleteTask(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            TaskInfoData data = dataHandler.getTaskInfoData(input);
            log.info("After handler input data is {}", data);
            analysisTaskService.deleteTask(data.getTaskId());
            result = EAResult.buildSuccessResult();
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getTask")
    @ResponseBody
    public EAResult getTask(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            TaskInfoData data = dataHandler.getTaskInfoData(input);
            log.info("After handler input data is {}", data);
            result = EAResult.buildSuccessResult(analysisTaskService.getTask(data.getTaskId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("clearTask")
    @ResponseBody
    public EAResult clearTask(){
        EAResult result = null;
        try{
            log.info("After handler input data is : null");
            analysisTaskService.clearTask();
            result = EAResult.buildSuccessResult();
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }


}
