package com.chanson.f2f;

// [加入编程导航](https://www.code-nav.cn/) 深耕编程提升【两年半】、国内净值【最高】的编程社群、用心服务【20000+】求学者、帮你自学编程【不走弯路】

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chanson.f2f.mapper")

public class F2FApplication {
    public static void main(String[] args) {

        SpringApplication.run(F2FApplication.class, args);
    }
}

