package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.common.KeywordData;
import com.sunrun.emailanalysis.data.request.custom.dictionary.CustomDictionaryData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.CustomDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("keyword")
@RestController
public class KeywordController {

    @Autowired
    private EADataHandler eaDataHandler;

    @Autowired
    private CustomDictionaryService customDictionaryService;





    @RequestMapping("addKeyword")
    public EAResult addKeyword(@RequestBody HashMap<String, Object> input){
        EAResult result;
        try{
            CustomDictionaryData data = eaDataHandler.handlerCustomDictionaryData(input);
            result = customDictionaryService.addCustomDictionary(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("updateKeyword")
    public EAResult updateKeyword(@RequestBody HashMap<String, Object> input){
        EAResult result;
        try{
            CustomDictionaryData data = eaDataHandler.handlerCustomDictionaryData(input);
            result = customDictionaryService.updateCustomDictionary(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("deleteKeyword")
    public EAResult deleteKeyword(@RequestBody HashMap<String, Object> input){
        EAResult result;
        try{
            IDData data = eaDataHandler.handlerIDData(input);
            result = customDictionaryService.deleteCustomDictionary(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("clearKeyword")
    public EAResult clearKeyword(){
        EAResult result;
        try{
            result = customDictionaryService.clearCustomDictionary();
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getKeywordListCount")
    public EAResult getCustomDictionaryListCount(@RequestBody HashMap<String, Object> input){
        EAResult result;
        try{
            KeywordData data = eaDataHandler.handlerKeywordData(input);
            result = EAResult.buildSuccessResult(customDictionaryService.getCustomDictionaryListCount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getKeywordList")
    public EAResult getCustomDictionaryList(@RequestBody HashMap<String, Object> input){
        EAResult result;
        try{
            KeywordData data = eaDataHandler.handlerKeywordData(input);
            result = EAResult.buildSuccessResult(customDictionaryService.getCustomDictionaryList(data));
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
