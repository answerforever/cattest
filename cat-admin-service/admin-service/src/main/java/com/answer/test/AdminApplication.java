package com.answer.test;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.answer.test"})
@EnableDiscoveryClient
@EnableFeignClients
@ImportResource({"classpath:spring-mybatis.xml"})
@EnableSwagger2Doc
//@MapperScan("com.answercloud.demo.mapper")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
