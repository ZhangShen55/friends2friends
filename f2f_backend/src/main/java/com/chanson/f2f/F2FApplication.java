package com.chanson.f2f;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.chanson.f2f.mapper")
@EnableScheduling
public class F2FApplication {
    public static void main(String[] args) {
        SpringApplication.run(F2FApplication.class, args);
    }
}

