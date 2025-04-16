package com.fu.springboot3demo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务demo
 *
 * @since 2024-08-12
 */
@Slf4j
@Component
public class MyScheduler {

    @Scheduled(fixedRate = 1000)
    public void task1() {
        Thread thread = Thread.currentThread();
        log.info("task1 name : {}, id : {}", thread.getName(), thread.threadId());
    }

    @Scheduled(fixedRate = 1000)
    public void task2() {
        Thread thread = Thread.currentThread();
        log.info("task2 name : {}, id : {}", thread.getName(), thread.threadId());
    }

    @Scheduled(fixedRate = 1000)
    public void task3() {
        Thread thread = Thread.currentThread();
        log.info("task3 name : {}, id : {}", thread.getName(), thread.threadId());
    }

    @Scheduled(fixedRate = 1000)
    public void task4() {
        Thread thread = Thread.currentThread();
        log.info("task4 name : {}, id : {}", thread.getName(), thread.threadId());
    }

    @Scheduled(fixedRate = 1000)
    public void task5() {
        Thread thread = Thread.currentThread();
        log.info("task5 name : {}, id : {}", thread.getName(), thread.threadId());
    }

}
