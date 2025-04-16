package com.fu.springboot3demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 全局过滤器，过滤请求。
 */
@Slf4j
@Component
@WebFilter(filterName = "globalFilter", urlPatterns = "/*")
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        // 继续执行请求链
        filterChain.doFilter(request, response);

        // 请求方法
        String method = request.getMethod();
        // URI
        String uri = request.getRequestURI();
        // 请求体
        byte[] requestContent = request.getContentAsByteArray();

        //过滤请求内容，去除空格和换行符
        String requestBodyString = new String(requestContent, StandardCharsets.UTF_8).replaceAll("\\s+", " ");
        log.info("请求内容:\nmethod: {}\nuri: {}\nrequest: {}", method, uri, requestBodyString);

        // 响应状态
        int status = response.getStatus();
        // 响应体
        byte[] responseContent = response.getContentAsByteArray();

        log.info("响应内容:\nstatus: {}\nresponse: {}", status, new String(responseContent, StandardCharsets.UTF_8));

        // 把缓存的响应数据，响应给客户端
        response.copyBodyToResponse();
    }

}
