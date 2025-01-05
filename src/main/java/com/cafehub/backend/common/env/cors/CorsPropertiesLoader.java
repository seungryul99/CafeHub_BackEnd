package com.cafehub.backend.common.env.cors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("spring.cors")
public class CorsPropertiesLoader {
    private final String allowOrigin;
    private final String allowMethods;
    private final String allowHeaders;
    private final String allowCredentials;
    private final String preflightCacheAge;
}
