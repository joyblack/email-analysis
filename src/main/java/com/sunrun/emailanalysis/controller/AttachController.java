package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.attach.AttachListData;
import com.sunrun.emailanalysis.data.request.entity.EditEntityData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.AttachService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RequestMapping("attach")
@Controller
public class AttachController {
    private static Logger log = LoggerFactory.getLogger(AttachController.class);
    @Autowired
    private AttachService attachService;

    @Autowired
    private EADataHandler dataHandler;

    @RequestMapping("getAttachListCount")
    @ResponseBody
    public EAResult getAttachListCount(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AttachListData attachListData = dataHandler.getAttachListData(input);
            log.info("After handler data: {}", attachListData);
            result = EAResult.buildSuccessResult(attachService.getAttachListCount(attachListData));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAttachList")
    @ResponseBody
    public EAResult getAttachList(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            AttachListData attachListData = dataHandler.getAttachListData(input);
            log.info("After handler data: {}", attachListData);
            result = EAResult.buildSuccessResult(attachService.getAttachList(attachListData));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("getEmailAttach")
    @ResponseBody
    public EAResult getEmailAttach(@RequestBody IDData data){
        EAResult result = null;
        try{
            log.info("Handler data: {}", data);
            result = EAResult.buildSuccessResult(attachService.getEmailAttach(data.getId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAttach")
    @ResponseBody
    public EAResult getAttach(@RequestBody IDData data){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(attachService.getAttach(data.getId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getAttachContent")
    @ResponseBody
    public EAResult getAttachContent(@RequestBody IDData data){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(attachService.getAttachContent(data.getId()));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    // Download Attach File.
    @RequestMapping("download/{attachId}")
    public void download(@PathVariable("attachId") Long attachId, HttpServletResponse response) throws IOException {
        try{
            attachService.download(attachId, response);
        } catch (EAException e) {
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    /* == Attach entities ==*/
    // Add the entity list to attachment, this just store the entity.
    @RequestMapping("editEntities")
    @ResponseBody
    public EAResult editEntities(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            EditEntityData data = dataHandler.getEditEntityData(input);
            log.info("After handler input data: {}", data);
            result = attachService.editEntities(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("getEditEntities")
    @ResponseBody
    public EAResult getEditEntities(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            IDData data = dataHandler.handlerIDData(input);
            log.info("After handler input data: {}", data);
            result = EAResult.buildSuccessResult(attachService.getEditEntities(data));
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("deleteRemarkEntity")
    @ResponseBody
    public EAResult deleteRemarkEntity(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            IDData data = dataHandler.handlerIDData(input);
            log.info("Handler data: {}", data);
            result = attachService.deleteRemarkEntity(data);
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("clearRemarkEntities")
    @ResponseBody
    public EAResult clearRemarkEntities(@RequestBody HashMap<String, Object> input){
        EAResult result = null;
        try{
            IDData data = dataHandler.handlerIDData(input);
            log.info("Handler data: {}", data);
            result = attachService.clearRemarkEntities(data);
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
