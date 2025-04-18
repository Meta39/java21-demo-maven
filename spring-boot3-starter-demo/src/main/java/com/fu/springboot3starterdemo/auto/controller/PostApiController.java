package com.fu.springboot3starterdemo.auto.controller;

import com.fu.springboot3starterdemo.api.PostApi;
import com.fu.springboot3starterdemo.auto.bean.BeanUtils;
import com.fu.springboot3starterdemo.dto.Res;
import com.fu.springboot3starterdemo.exception.PostApiException;
import com.fu.springboot3starterdemo.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostApiController {

    /**
     * 统一接口入口
     *
     * @param serviceName bean名称
     * @param body     请求体
     */
    @PostMapping(value = "/allApiEntrance")
    @SuppressWarnings("unchecked")
    public <T> Res<?> allApiEntrance(@RequestParam("serviceName") String serviceName, @RequestBody String body) {
        log.info("{}入参:{}", serviceName, body);
        PostApi<T> postApi;
        try {
            postApi = (PostApi<T>) BeanUtils.getBean(serviceName, PostApi.class);
        } catch (BeansException e) {
            return Res.error(serviceName + "接口不存在");
        }
        Class<T> requestClass = (Class<T>) BeanUtils.BEAN_TYPE_MAP.get(serviceName);
        T requestType = JacksonUtils.jsonToObject(body, requestClass);
        Object response;
        try {
            response = postApi.execute(requestType);
        } catch (PostApiException e) {
            Integer code = e.getCode();
            String message = e.getMessage();
            log.error("{}异常:", e.getMessage(), e);
            if (Objects.isNull(code)) {
                return Res.error(message);
            }
            return Res.error(code, message);
        } catch (Exception e) {
            log.error("{}异常:", e.getMessage(), e);
            return Res.error(e.getMessage());
        }
        log.info("{}出参:{}", serviceName, response);
        return Res.ok(response);
    }

}
