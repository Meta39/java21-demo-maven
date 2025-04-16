package com.fu.springboot3demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * caffeine 缓存使用
 * 创建日期：2024-06-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheManager cacheManager;

    /**
     * Cacheable 用于查询
     * 如果缓存中有对应的key，则直接从缓存中获取对应key的value并返回。
     * 如果缓存中没有对应的key或key过期，则把缓存反参到对应key。
     */
    @Cacheable(cacheNames = "cache1", key = "#cacheKey")//value 的值 cache1 要和 application.yml 配置的 cache-names 一致
    public String getCache(String cacheKey) throws InterruptedException {
        // 模拟一个耗时的方法，如果缓存有数据，直接取缓存，否则3秒后返回。
        Thread.sleep(3000);
        return "Data for " + cacheKey;
    }

    /**
     * CachePut 用于新增/修改
     * 始终把反参放入对应的key
     */
    @CachePut(cacheNames = "cache1", key = "#cacheKey")
    public String putCache(String cacheKey) {
        log.info("把返回内容写入对应的key：{}", cacheKey);
//        return null;
        return cacheKey + ThreadLocalRandom.current().nextDouble();
    }

    /**
     * CacheEvict 用于删除数据
     * 删除缓存
     */
    @CacheEvict(cacheNames = "cache1", key = "#cacheKey")
    public void removeCache(String cacheKey) {
        log.info("删除缓存：{}", cacheKey);
    }

    public Object getCacheByKey(String cacheKey) {
        Object value = cacheManager.getCache("cache1").get(cacheKey).get();
        log.info("{}",value);
        return value;
    }

}
