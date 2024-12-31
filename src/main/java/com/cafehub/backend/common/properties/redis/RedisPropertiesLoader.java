package com.cafehub.backend.common.properties.redis;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// Q. 이거 @Component 없어도 상관 없을까?
@Getter
@ConfigurationProperties("spring.data.redis")
public class RedisPropertiesLoader {
    private final String host;
    private final int port;

    public RedisPropertiesLoader(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
