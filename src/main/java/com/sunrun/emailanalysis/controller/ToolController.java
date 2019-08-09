package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.tool.AddressData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static ucar.httpservices.HTTPSession.log;

@RequestMapping("tool")
@RestController
public class ToolController {
    @Autowired
    private EADataHandler dataHandler;

    @Autowired
    private ToolService toolService;

    @RequestMapping("getAddressByIP")
    @ResponseBody
    public EAResult getAddressByIP(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AddressData data = dataHandler.getAddressData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(toolService.getAddressByIP(data.getIp()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAddressByAddress")
    @ResponseBody
    public EAResult getAddressByAddress(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AddressData data = dataHandler.getAddressData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(toolService.getAddressByAddress(data.getAddress()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAddressByZipCode")
    @ResponseBody
    public EAResult getAddressByZipCode(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AddressData data = dataHandler.getAddressData(input);
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(toolService.getAddressByZipCode(data.getZipCode()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }



    @RequestMapping("gc")
    @ResponseBody
    public EAResult gc(){
        EAResult result = null;
        try{
            System.gc();
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
