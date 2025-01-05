package com.cafehub.backend.common.env.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedisProperties {

    private final String host;
    private final int port;
}
