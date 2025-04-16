package com.fu.springboot3demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * jackson 工具类
 */
public abstract class JacksonUtils {
    public static final ObjectMapper JSON;
    public static final ObjectMapper XML;

    static {
        // json 配置
        JSON = new ObjectMapper();
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON.registerModule(new JavaTimeModule());//处理java8新日期时间类型
        // xml 配置
        XML = new XmlMapper();
        XML.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        XML.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        XML.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        XML.registerModule(new JavaTimeModule());//处理java8新日期时间类型
    }

}