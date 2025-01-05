package com.cafehub.backend.common.env.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("spring.jwt")
public class JwtPropertiesLoader {

    private final String Secret;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
}
