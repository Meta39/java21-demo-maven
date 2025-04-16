package com.fu.springboot3demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.springboot3demo.generic.GenericService;
import com.fu.springboot3demo.generic.GenericServiceTypeCache;
import com.fu.springboot3demo.generic.R;
import com.fu.springboot3demo.util.ApplicationContextUtils;
import com.fu.springboot3demo.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 通用入口
 *
 * @since 2024-07-16
 */
@Slf4j
@RestController
@RequestMapping("api")
public class GenericController {

    @PostMapping("/{serviceName}")
    @SuppressWarnings("unchecked")
    public <T> Object invokeService(@PathVariable String serviceName, @RequestBody(required = false) String requestBody) throws JsonProcessingException {
//        log.info("{}接口入参：{}", serviceName, requestBody);
        GenericService<T> genericService = (GenericService<T>) ApplicationContextUtils.getBean(serviceName);
        //通过缓存获取对应 GenericService 实现类 invoke 函数的具体泛型入参。
        Class<T> requestType = (Class<T>) GenericServiceTypeCache.getRequestType(serviceName);
        // 将 JSON 字符串请求参数转换为具体的类型
        T reqObject = JacksonUtils.JSON.readValue(requestBody, requestType);
        try {
            return R.ok(genericService.invoke(reqObject));
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(e.getMessage(), e);
            return R.err(message);
        }
    }

}