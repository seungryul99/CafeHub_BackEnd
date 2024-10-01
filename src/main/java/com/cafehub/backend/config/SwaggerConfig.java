package com.cafehub.backend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("CafeHub Backend API Document")
                .version("v1.0.0")
                .description("CafeHub Backend의 API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}