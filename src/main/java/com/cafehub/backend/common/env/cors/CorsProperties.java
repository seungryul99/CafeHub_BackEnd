package com.cafehub.backend.common.env.cors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CorsProperties {
    private final String allowOrigin;
    private final String allowMethods;
    private final String allowHeaders;
    private final String allowCredentials;
    private final String preflightCacheAge;
}
