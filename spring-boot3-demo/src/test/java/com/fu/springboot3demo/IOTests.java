package com.fu.springboot3demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class IOTests {

    /**
     * jdk17 try-with写法自动关闭流
     */
    @Test
    void test() {
        /*try (FileInputStream fis = new FileInputStream("D:/a.txt")) {
            int read = fis.read();
            log.info("读取内容：{}", read);
        } catch (IOException e) {
            log.error("文件读取异常", e);
            throw new RuntimeException("文件读取异常");
        }*/
    }

}
