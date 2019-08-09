package com.sunrun.emailanalysis.config;

import com.sunrun.emailanalysis.task.info.TaskInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 配置SEA维护的任务
 */
@Configuration
public class TaskConfig {
    @Bean
    public HashMap<Long, TaskInfo> analysisTask(){
        return new HashMap<>();
    }
}
