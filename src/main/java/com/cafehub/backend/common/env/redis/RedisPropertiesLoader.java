package com.cafehub.backend.common.env.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


// Q. 이거 @Component 없어도 상관 없을까?
@Getter
@RequiredArgsConstructor
@ConfigurationProperties("spring.data.redis")
public class RedisPropertiesLoader {
    private final String host;
    private final int port;
}

