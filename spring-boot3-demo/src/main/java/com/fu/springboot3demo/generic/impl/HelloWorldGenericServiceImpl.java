package com.fu.springboot3demo.generic.impl;

import com.fu.springboot3demo.dto.GenericServiceDTO;
import com.fu.springboot3demo.generic.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @since 2024-07-16
 */
@Slf4j
@Service("helloWorld")
public class HelloWorldGenericServiceImpl implements GenericService<GenericServiceDTO> {

    @Override
    @Transactional
    public String invoke(GenericServiceDTO req) {
        log.info("GenericServiceDto: {}", req);
        /*if (true) {
            throw new RuntimeException("运行时异常");
        }*/
        return "Hello World";
    }

}