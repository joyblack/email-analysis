package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.account.warning.SetStateData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.WarningAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequestMapping("warningAccount")
@Controller
public class WarningAccountController {
    private static Logger log = LoggerFactory.getLogger(WarningAccountController.class);
    @Autowired
    private WarningAccountService warningAccountService;

    @Autowired
    private EADataHandler dataHandler;

    @RequestMapping("add")
    @ResponseBody
    public EAResult add(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            WarningAccountData data = dataHandler.getWarningAccountData(input);
            log.info("Handler data: {}", data);
            result = warningAccountService.add(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("edit")
    @ResponseBody
    public EAResult edit(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            WarningAccountData data = dataHandler.getWarningAccountData(input);
            log.info("Handler data: {}", data);
            result = warningAccountService.edit(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("delete")
    @ResponseBody
    public EAResult delete(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            IDData data = dataHandler.handlerIDData(input);
            log.info("Handler data: {}", data);
            result = warningAccountService.delete(data);
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
    @ResponseBody
    public EAResult getAccountListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{

            WarningAccountListData data = dataHandler.handlerWarningAccountListData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(warningAccountService.getAccountListCount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAccountList")
    @ResponseBody
    public EAResult getAccountList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            WarningAccountListData data = dataHandler.handlerWarningAccountListData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(warningAccountService.getAccountList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("setState")
    @ResponseBody
    public EAResult setState(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            SetStateData data = dataHandler.handlerSetStateData(input);
            log.info("Handler data: {}", data);
            result = warningAccountService.setState(data);
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
