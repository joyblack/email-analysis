package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("fileSystem")
@RestController
public class FileSystemController {
    @Autowired
    private FileSystemService fileSystemService;

    @RequestMapping("getSupportProtocol")
    public EAResult getSupportProtocol(){
        EAResult result = null;
        try{
            result = EAResult.buildSuccessResult(fileSystemService.getSupportProtocol());
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
