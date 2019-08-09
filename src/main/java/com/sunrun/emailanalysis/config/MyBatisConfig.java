package com.sunrun.emailanalysis.config;

import org.apache.commons.logging.impl.SLF4JLog;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.session.Configuration;
// 注意不是org.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;

@MapperScan("com.sunrun.emailanalysis.mapper")
@org.springframework.context.annotation.Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer(){
            @Override
            public void customize(Configuration configuration) {
                // 开启驼峰映射规则
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.setLogImpl(Slf4jImpl.class);
//                configuration.setLogImpl(Log4j);
                // 启用缓存configuration.setCacheEnabled(true);
                //configuration.setCallSettersOnNulls(true);
            }
        };
    }
}
