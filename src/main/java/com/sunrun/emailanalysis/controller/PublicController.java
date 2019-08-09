package com.sunrun.emailanalysis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    /**
     * 首页映射
     * @return
     */
    @RequestMapping({"/","index",""})
    public String index(){
        return "服务成功开启...";
    }


}
