package com.sunrun.emailanalysis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("about")
@Controller
public class AboutController {
    @RequestMapping({"/","index","about",""})
    public String index(){
        return "about";
    }
}
