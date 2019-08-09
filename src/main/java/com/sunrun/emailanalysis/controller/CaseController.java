package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.cases.CaseData;
import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.CaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("case")
@RestController
public class CaseController {

    private static Logger log = LoggerFactory.getLogger(CaseController.class);

    @Autowired
    private EADataHandler eaDataHandler;

    @Autowired
    private CaseService caseService;

    @RequestMapping("getCaseList")
    public EAResult getCaseList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            CaseListData data = eaDataHandler.getCaseListData(input);
            log.info("After handler input data is {}", data);
            result = EAResult.buildSuccessResult(caseService.getCaseList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAllCaseType")
    public EAResult getAllCaseType(){
        EAResult result = null;
        try {
            result = EAResult.buildSuccessResult(caseService.getAllCaseType());
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getCaseByName")
    public EAResult getCaseByName(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            CaseData data = eaDataHandler.getCaseData(input);
            result = EAResult.buildSuccessResult(caseService.getCaseByName(data.getCaseName()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getCaseById")
    public EAResult getCaseById(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            CaseData data = eaDataHandler.getCaseData(input);
            result = EAResult.buildSuccessResult(caseService.getCaseById(data.getCaseId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("clearCase")
    public EAResult clearCase(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try {
            IDData idData = eaDataHandler.handlerIDData(input);
            result = caseService.clearCase(idData.getId());
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
