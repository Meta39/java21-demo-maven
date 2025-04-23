package com.fu.springboot3starterusedemo.api;

import com.fu.springboot3starterdemo.api.PostApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestVoid implements PostApi<String> {

    /**
     * 返回值类型任意，因为不需要处理。
     */
    @Override
    public Object execute(String request) {
        return "我收到了你发送的请求内容:" + request;
    }

}
