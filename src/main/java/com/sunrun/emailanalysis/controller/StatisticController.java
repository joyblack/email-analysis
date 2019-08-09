package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.statistic.AttachTypeData;
import com.sunrun.emailanalysis.data.request.statistic.EmailTotalData;
import com.sunrun.emailanalysis.data.request.statistic.StatisticRelationData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("statistic")
@RestController
public class StatisticController {
    private static Logger log = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private EADataHandler dataHandler;

    @RequestMapping("getCaseTotal")
    public EAResult getCaseTotal(){
        EAResult result = null;
        try{
            Long total = statisticService.getCaseTotal();
            HashMap<String, Long> data = new HashMap<>();
            data.put(ResultDictionary.TOTAL, total);
            result = EAResult.buildSuccessResult(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getEmailTotal")
    public EAResult getEmailTotal(@RequestBody(required = false) HashMap<String, Object> input){
        EAResult result = null;
        try{
            Long caseId = null;
            if(input != null){
                EmailTotalData emailTotalData = dataHandler.getEmailTotalData(input);
                caseId = emailTotalData.getCaseId();
            }
            Long total = statisticService.getEmailTotal(caseId);
            HashMap<String, Long> data = new HashMap<>();
            data.put(ResultDictionary.TOTAL, total);
            result = EAResult.buildSuccessResult(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // ===================== attachment ==========================
    @RequestMapping("attachType")
    public EAResult attachType(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AttachTypeData data = dataHandler.getAttachTypeData(input);
            log.info("After handler input data：{}", data);
            result = EAResult.buildSuccessResult(statisticService.getAttachType(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }


    // ================== Email Relationship ==============================
    // This is the `getStatisticRelationList`'s accompanying interface.
    @RequestMapping("getStatisticRelationListCount")
    public EAResult getStatisticRelationListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            StatisticRelationData data = dataHandler.getRelationData(input);
            log.info("After handler data is: {}", data);
            result = EAResult.buildSuccessResult(statisticService.getStatisticRelationListCount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get the statistic relation list(divide.)
    @RequestMapping("getStatisticRelationList")
    public EAResult getStatisticRelationList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            StatisticRelationData data = dataHandler.getRelationData(input);
            log.info("After handler data is: {}", data);
            result = EAResult.buildSuccessResult(statisticService.getStatisticRelationList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get the statistic relation total(combine.)
    @RequestMapping("getStatisticRelationTotal")
    public EAResult getStatisticRelationTotal(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            StatisticRelationData data = dataHandler.getRelationData(input);
            log.info("After handler data is: {}", data);
            result = EAResult.buildSuccessResult(statisticService.getStatisticRelationTotal(data));
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
