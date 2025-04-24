package com.fu.springboot3demo.controller;

import com.fu.springboot3demo.generic.GenericService;
import com.fu.springboot3demo.generic.GenericServiceTypeCache;
import com.fu.springboot3demo.generic.R;
import com.fu.springboot3demo.util.ApplicationContextUtils;
import com.fu.springboot3demo.util.JacksonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通用入口
 *
 * @since 2024-07-16
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GenericController {
    private final Validator validator;

    @PostMapping(value = "/allApiEntrance")
    @SuppressWarnings("unchecked")
    public <T> Object invokeService(@RequestParam(name = "serviceName") String serviceName, @RequestBody(required = false) String requestBody) {
//        log.info("{}接口入参：{}", serviceName, requestBody);
        GenericService<T> genericService = (GenericService<T>) ApplicationContextUtils.getBean(serviceName);
        //通过缓存获取对应 GenericService 实现类 invoke 函数的具体泛型入参。
        Class<T> requestType = (Class<T>) GenericServiceTypeCache.getRequestType(serviceName);
        // 将 JSON 字符串请求参数转换为具体的类型
        T reqObject = JacksonUtils.jsonToObject(requestBody, requestType);

        // 手动参数校验
        Set<ConstraintViolation<T>> violations = validator.validate(reqObject);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            return R.err(errorMessage);
        }

        try {
            return R.ok(genericService.invoke(reqObject));
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(e.getMessage(), e);
            return R.err(message);
        }
    }

}