package com.fu.springboot3demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.springboot3demo.generic.response.Response1;
import com.fu.springboot3demo.generic.response.Response2;
import com.fu.springboot3demo.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @since 2024-08-14
 */
@Slf4j
public class BaseResponseTests {

    @Test
    void testBaseResponse() throws JsonProcessingException {
        Response1 response1 = new Response1().ok();
        response1.setId(1L);

        Response1 error1 = new Response1().err("error1");

        Response2 response2 = new Response2().ok();
        response2.setUsername("root");

        Response2 error2 = new Response2().err("error2");

        log.info("Response1:{}\nResponse2:{}", JacksonUtils.JSON.writeValueAsString(response1), JacksonUtils.JSON.writeValueAsString(response2));
        log.error("Response1 error:{}\nResponse2 error:{}", JacksonUtils.JSON.writeValueAsString(error1), JacksonUtils.JSON.writeValueAsString(error2));
    }

}
