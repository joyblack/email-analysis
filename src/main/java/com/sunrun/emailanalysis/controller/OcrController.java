package com.sunrun.emailanalysis.controller;


import com.sunrun.emailanalysis.data.handler.EADataHandler;
import com.sunrun.emailanalysis.data.request.entity.EditEntityData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequestMapping("ocr")
@Controller
public class OcrController {
    private static Logger log = LoggerFactory.getLogger(OcrController.class);

    @Autowired
    private OcrService ocrService;

    @Autowired
    private EADataHandler eaDataHandler;

    @RequestMapping("ocrImgByAttachId")
    @ResponseBody
    public EAResult ocrImgByAttachId(@RequestBody HashMap<String, Object> input) {
        EAResult result = null;
        try {
            IDData data = eaDataHandler.handlerIDData(input);
            log.info("After handler data: {}", data);
            result = ocrService.ocrImgByAttachId(data.getId());
            return result;
        } catch (EAException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
