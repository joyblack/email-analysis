package com.sunrun.emailanalysis.controller;

import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.po.User;
import com.sunrun.emailanalysis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录映射
     * @return
     */
    @RequestMapping({"login"})
    public String login(){
        return "login/login";
    }

    /**
     * 登录请求
     * @return
     */
    @RequestMapping({"loginByUser"})
    @ResponseBody
    public EAResult loginByUser(@RequestBody User user){
//        System.out.println(user);
//        User loginUser = userService.getUser(user.getLoginName(),user.getPassword());
//        System.out.println(user);
//        if(loginUser != null){
//            return new EAResult(Notice.LOGIN_SUCCESS,true, loginUser);
//        }else{
//            return new EAResult(Notice.LOGIN_PASSWORD_IS_ERROR,false, user);
//        }
        return null;
    }

  
}
