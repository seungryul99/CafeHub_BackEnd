package com.cafehub.backend.common.properties.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisProperties {

    private final String host;
    private final int port;
}
