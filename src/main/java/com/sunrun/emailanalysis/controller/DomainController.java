package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.domain.DomainListData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("domain")
@RestController
public class DomainController {

    private static Logger log = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    private EADataHandler eaDataHandler;

    @Autowired
    private DomainService domainService;

    @RequestMapping("getDomainList")
    public EAResult getDomainList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            DomainListData data = eaDataHandler.getDomainListData(input);
            log.info("After handler input data is {}", data);
            result = EAResult.buildSuccessResult(domainService.getDomainList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getDomainListCount")
    public EAResult getDomainListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            DomainListData data = eaDataHandler.getDomainListData(input);
            log.info("After handler input data is {}", data);
            result = EAResult.buildSuccessResult(domainService.getDomainListCount(data));
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
