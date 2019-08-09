package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.request.account.AccountListData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.po.EmailCase;
import com.sunrun.emailanalysis.service.common.RefreshTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("hack")
public class HackController {

    @Autowired
    private RefreshTableService refreshTableService;

    @RequestMapping("refreshDomainAndAccountTable")
    public EAResult refreshDomainAndAccountTable(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EmailCase emailCase = new EmailCase();
            emailCase.setCaseId(Long.valueOf(input.get("caseId").toString()));
            refreshTableService.updateAccountAndDomain(emailCase);
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
