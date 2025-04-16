package com.fu.springboot3demo.config.scheduler;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 自定义定时任务线程池
 *
 * @since 2024-08-12
 */
@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "scheduler")
public class ThreadPoolTaskSchedulerConfig {
    private int poolSize = 1;
    private String threadNamePrefix = "myThreadPoolTaskScheduler";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.initialize();
        log.info("scheduler.poolSize={}", poolSize);
        return scheduler;
    }

}
