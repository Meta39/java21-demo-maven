package com.fu.springboot3demo.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RestClient 和 RestTemplate http 客户端配置
 */
@Configuration
public class RestClientAndRestTemplateConfig {
    @Value("${rest.base-url}")
    private String baseUrl;

    /**
     * Spring 新一代 http 客户端，使用 Apache http 连接池
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setAcceptCharset(List.of(StandardCharsets.UTF_8));//默认：请求的编码为 UTF-8
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);//默认：请求的内容类型为 JSON
                })
                .defaultRequest(requestHeadersSpec -> {
                    requestHeadersSpec.acceptCharset(StandardCharsets.UTF_8);//默认：反参编码格式
                    requestHeadersSpec.accept(MediaType.APPLICATION_JSON);//默认：只接收JSON格式的反参
                })
                .requestFactory(new HttpComponentsClientHttpRequestFactory(HttpClients.custom()
                        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnTotal(100) // 最大连接数，默认：25个
                                .setMaxConnPerRoute(10)// 每个路由的最大连接数，默认：5个
                                .build()
                        )
                        .setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofMinutes(1))// 连接超时时间，默认：3分钟
                                .setResponseTimeout(Timeout.ofMinutes(1))// 设置响应超时时间，默认：3分钟
                                .build())
                        .build()))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClients.custom()
                        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnTotal(100) // 最大连接数，默认：25个
                                .setMaxConnPerRoute(10)// 每个路由的最大连接数，默认：5个
                                .build())
                        .setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofMinutes(1))// 连接超时时间，默认：3分钟
                                .setResponseTimeout(Timeout.ofMinutes(1))// 设置响应超时时间，默认：3分钟
                                .build())
                        .build()))
                .build();
        //设置请求字符串的编码格式为UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

}
