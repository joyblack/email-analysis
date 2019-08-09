package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.mapper.SysConfigMapper;
import com.sunrun.emailanalysis.po.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigService {
    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 根据配置名获取配置的值
     * @param configName
     * @return
     */
    public String getConfigValue(String configName){
        System.out.println(configName);
        SysConfig condition = new SysConfig();
        condition.setConfigName(configName);
        SysConfig config = sysConfigMapper.selectOne(condition);
        if(config == null){
            return null;
        }else{
            return config.getConfigValue();
        }
    }


}
