package com.fu.springboot3demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * jackson 工具类
 */
public abstract class JacksonUtils {
    private static final ObjectMapper JSON;
    private static final ObjectMapper XML;

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

    /**
     * 对象转JSON
     *
     * @param object 对象
     * @return JSON
     */
    public static String objectToJson(Object object) {
        try {
            return JSON.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON转对象
     *
     * @param json  JSON
     * @param clazz 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return JSON.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON转对象
     *
     * @param json  JSON
     * @param typeReference 指定泛型对象类型
     */
    public static <T> T jsonToObject(String json, TypeReference<T> typeReference) {
        try {
            return JSON.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转XML
     *
     * @param object 对象
     * @return XML
     */
    public static String objectToXml(Object object) {
        try {
            return XML.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * XML转对象
     *
     * @param xml  XML
     * @param clazz 对象
     */
    public static <T> T xmlToObject(String xml, Class<T> clazz) {
        try {
            return XML.readValue(xml, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * XML转对象
     *
     * @param xml  XML
     * @param typeReference 指定泛型对象类型
     */
    public static <T> T xmlToObject(String xml, TypeReference<T> typeReference) {
        try {
            return XML.readValue(xml, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}