package com.fu.springboot3demo.controller;

import com.fu.springboot3demo.service.CacheService;
import com.fu.springboot3demo.service.CacheService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本地缓存
 * 创建日期：2024-06-04
 */
@RequestMapping("cache")
@RestController
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;
    private final CacheService2 cacheService2;//只是使用了@CacheConfig(cacheNames = "cache2")，其它并无不同

    @GetMapping("put")
    public String put(@RequestParam("value") String value) {
        return cacheService.putCache(value);
    }

    @GetMapping("get")
    public String get(@RequestParam("value") String value) throws InterruptedException {
        return cacheService.getCache(value);
    }

    @GetMapping("remove")
    public void remove(@RequestParam("value") String value) {
        cacheService.removeCache(value);
    }

    @GetMapping
    public Object getCacheByKey(String cacheKey) {
        return cacheService.getCacheByKey(cacheKey);
    }

}
