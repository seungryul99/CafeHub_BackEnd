package com.cafehub.backend.common.env.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CorsPropertiesConfig {

    private final CorsPropertiesLoader corsPropertiesLoader;

    @Bean
    public CorsProperties corsProperties(){
        return new CorsProperties(
                corsPropertiesLoader.getAllowOrigin(),
                corsPropertiesLoader.getAllowMethods(),
                corsPropertiesLoader.getAllowHeaders(),
                corsPropertiesLoader.getAllowCredentials(),
                corsPropertiesLoader.getPreflightCacheAge()
        );
    }
}
