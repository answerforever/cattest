package com.answer.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.answer.test"})
@EnableDiscoveryClient
@ImportResource({"classpath:spring-mybatis.xml"})
//@MapperScan("com.answercloud.demo.mapper")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
