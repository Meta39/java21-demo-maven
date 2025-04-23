package com.fu.springboot3starterusedemo.api;

import com.fu.springboot3starterdemo.api.PostApi;
import com.fu.springboot3starterusedemo.dto.ValidParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestValidParams  implements PostApi<ValidParams> {

    /**
     * 验证参数校验是否生效
     */
    @Override
    public Object execute(ValidParams request) {
        return request;
    }

}
