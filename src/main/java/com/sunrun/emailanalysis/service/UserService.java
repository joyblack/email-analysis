package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.mapper.UserMapper;
import com.sunrun.emailanalysis.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUser(String loginName, String password){
        User condition = new User();
        condition.setLoginName(loginName);
        condition.setPassword(password);
        return userMapper.selectOne(condition);
    }
}
