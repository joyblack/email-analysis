package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.analysis.EmailAnalysisData;
import com.sunrun.emailanalysis.data.request.analysis.MonitorData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailByAccountData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailByEntityData;
import com.sunrun.emailanalysis.data.request.email.SearchEmailData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.EmailService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RequestMapping("email")
@RestController
public class EmailController {
    private static Logger log = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private EmailService emailService;

    @Autowired
    private EADataHandler eaDataHandler;

    // Check the email check.
    @RequestMapping("analysisCheck")
    public EAResult analysisCheck(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EmailAnalysisData data = eaDataHandler.handlerAnalysisData(input);
            result = emailService.analysisCheck(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // 分析邮件
    @RequestMapping("analysis")
    public EAResult analysis(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EmailAnalysisData data = eaDataHandler.handlerAnalysisData(input);
            result = emailService.analysis(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // 查询当前的分析进度
    @RequestMapping("getMonitor")
    public EAResult getMonitor(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
             MonitorData data = eaDataHandler.handlerMonitorData(input);
             result = EAResult.buildSuccessResult(emailService.monitor(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get email list.
    @RequestMapping("getEmailListByEntityValue")
    public EAResult getEmailListByEntityValue(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailByEntityData data = eaDataHandler.getSearchEmailByEntityData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailListByEntityValue(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get
    @RequestMapping("getEmailListCountByEntityValue")
    public EAResult getEmailListCountByEntityValue(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailByEntityData data = eaDataHandler.getSearchEmailByEntityData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailListCountByEntityValue(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }


    // Get email list.
    @RequestMapping("getEmailList")
    public EAResult getEmailList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailData data = eaDataHandler.getSearchEmailData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get
    @RequestMapping("getEmailListCount")
    public EAResult getEmailListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailData data = eaDataHandler.getSearchEmailData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailListCount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get email list.
    @RequestMapping("getEmailListByAccount")
    public EAResult getEmailListByAccount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailByAccountData data = eaDataHandler.getSearchEmailByAccountData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailListByAccount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get email list.
    @RequestMapping("getEmailListCountByAccount")
    public EAResult getEmailListCountByAccount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            SearchEmailByAccountData data = eaDataHandler.getSearchEmailByAccountData(input);
            log.info("After handler request data is {}", input);
            result = EAResult.buildSuccessResult(emailService.getEmailListCountByAccount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }



    // 获取邮件基本信息
    @RequestMapping("getBasicInfo")
    public EAResult getBasicInfo(@RequestBody IDData data){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(emailService.getBasicInfo(data.getId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // 获取邮件收发件人关系数据
    @RequestMapping("getRelationData")
    public EAResult getRelationData(@RequestBody IDData data){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(emailService.getRelationData(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getContentInfo")
    public EAResult getContentInfo(@RequestBody IDData data){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(emailService.getContentInfo(data.getId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Download email file.
    @RequestMapping("download/{emailID}")
    public void download(@PathVariable("emailID") Long emailID, HttpServletResponse response) throws IOException {
        try{
            emailService.download(emailID, response);
        } catch (EAException e) {
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }
    }



}
