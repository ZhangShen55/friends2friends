package com.chanson.f2f.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Profile({"dev","test"}) //springboot启动 只有在dev和test环境时swagger接口管理工具才会生效 不要暴露项目的接口
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("用户中心")
                        .description("# 用户中心项目 APIs")
                        .termsOfServiceUrl("https://github.com/ZhangShen55")
                        .contact("mainuser@88.com")
                        .version("开天辟地版本")
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.chanson.f2f.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}