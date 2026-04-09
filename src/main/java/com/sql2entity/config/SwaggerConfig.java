package com.sql2entity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SQL2Entity API")
                        .version("1.0.0")
                        .description("SQL建表语句转换为Java Entity类的API服务")
                        .contact(new Contact()
                                .name("SQL2Entity")
                                .url("https://github.com/sql2entity"))
                        .license(new License()
                                .name("MIT License")));
    }
}
