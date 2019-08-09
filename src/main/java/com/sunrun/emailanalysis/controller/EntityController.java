package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.entity.EntityListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("entity")
@RestController
public class EntityController {

    private static Logger log = LoggerFactory.getLogger(EntityController.class);

    @Autowired
    private EntityService entityService;

    @Autowired
    private EADataHandler eaDataHandler;

    @RequestMapping("getEntityAppearPositionList")
    public EAResult getEntityAppearPositionList(){
        EAResult result = null;
        try{
            log.info("After handler input data: null");
            result = EAResult.buildSuccessResult(entityService.getEntityAppearPositionList());
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Get now system support entity type list.
    @RequestMapping("getEntityTypeList")
    public EAResult getEntityTypeList(){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(entityService.getEntityTypeList());
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Search entity list need to known the list total count.
    @RequestMapping("getEntityListCount")
    public EAResult getEntityListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EntityListData data = eaDataHandler.getEntityListData(input);
            log.info("After handler input data:  {}", data);
            result = EAResult.buildSuccessResult(entityService.getEntityListCount(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Before call this interface should call `getEntityListCount`
    @RequestMapping("getEntityList")
    public EAResult getEntityList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EntityListData data = eaDataHandler.getEntityListData(input);
            log.info("After handler input data: {}", data);
            result = EAResult.buildSuccessResult(entityService.getEntityList(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // By id to get the entity information.
    @RequestMapping("getEntityById")
    public EAResult getEntityById(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            IDData idData = eaDataHandler.handlerIDData(input);
            log.info("After handler input data:  {}", idData);
            result = EAResult.buildSuccessResult(entityService.getEntityById(idData));
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
