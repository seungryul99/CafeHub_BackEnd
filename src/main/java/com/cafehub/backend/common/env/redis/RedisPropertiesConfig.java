package com.cafehub.backend.common.env.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisPropertiesConfig {

    private final RedisPropertiesLoader redisPropertiesLoader;

    @Bean
    public RedisProperties redisProperties(){
        return new RedisProperties(
                redisPropertiesLoader.getHost(),
                redisPropertiesLoader.getPort()
        );
    }
}
