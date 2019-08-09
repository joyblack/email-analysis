package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.account.AccountListData;
import com.sunrun.emailanalysis.data.request.cases.CaseData;
import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.AccountService;
import com.sunrun.emailanalysis.service.CaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("account")
@RestController
public class AccountController {

    private static Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private EADataHandler eaDataHandler;

    @Autowired
    private AccountService accountService;

    @RequestMapping("getAccountList")
    public EAResult getAccountList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AccountListData data = eaDataHandler.getAccountListData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(accountService.getAccountList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAccountListCount")
    public EAResult getAccountListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AccountListData data = eaDataHandler.getAccountListData(input);
            log.info("After handler input data is {}", data);
            result = EAResult.buildSuccessResult(accountService.getAccountListCount(data));
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
