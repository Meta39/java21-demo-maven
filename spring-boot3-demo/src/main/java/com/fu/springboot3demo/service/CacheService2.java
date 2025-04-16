package com.fu.springboot3demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 创建日期：2024-06-04
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "cache2")
public class CacheService2 {

    /**
     * 把返回内容写入对应的key
     */
    @Cacheable(key = "#cacheKey")//value 的值 cache1 要和 application.yml 配置的 cache-names 一致
    public String getCache(String cacheKey) throws InterruptedException {
        // 模拟一个耗时的方法，如果缓存有数据，直接取缓存，否则3秒后返回。
        Thread.sleep(3000);
        return "Data for " + cacheKey;
    }

    /**
     * 把返回内容写入对应的key
     */
    @CachePut(key = "#cacheKey")
    public String putCache(String cacheKey) {
        log.info("设置缓存：{}", cacheKey);
        return cacheKey + ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 删除缓存
     */
    @CacheEvict(key = "#cacheKey")
    public void removeCache(String cacheKey) {
        log.info("删除缓存：{}", cacheKey);
    }

}
