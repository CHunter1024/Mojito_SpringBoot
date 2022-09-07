package com.freedom.mojito;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Description:
 * <p>CreateTime: 2022-07-12 下午 3:06</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@SpringBootApplication
@EnableCaching
public class MojitoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojitoApplication.class, args);
        log.info("项目启动完成......");
    }
}
