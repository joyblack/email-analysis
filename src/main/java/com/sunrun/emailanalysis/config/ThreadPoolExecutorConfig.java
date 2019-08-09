package com.sunrun.emailanalysis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
@Configuration
@EnableAsync
public class ThreadPoolExecutorConfig  {
    @Autowired
    private TaskThreadPoolConfig config;
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(config.getCorePoolSize());
        // 线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(config.getMaxPoolSize());
        // 线程池所使用的缓冲队列
       // taskExecutor.setQueueCapacity(config.getQueueCapacity());
        // 设置名字前缀
        taskExecutor.setThreadNamePrefix("EA-");
        // 线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        taskExecutor.initialize();
        return taskExecutor;
    }

}
