package com.cafehub.backend.common.env.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtProperties {

    private final String Secret;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
}
