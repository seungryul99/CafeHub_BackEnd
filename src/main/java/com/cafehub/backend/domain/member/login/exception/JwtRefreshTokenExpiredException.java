package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode.JWT_REFRESH_TOKEN_EXPIRED;

public class JwtRefreshTokenExpiredException extends CafeHubException {
    public JwtRefreshTokenExpiredException() {
        super(JWT_REFRESH_TOKEN_EXPIRED);
    }
}
