package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode.BLOCKED_JWT_REFRESH_TOKEN;

public class JwtRefreshTokenBlockedException extends CafeHubException {
    public JwtRefreshTokenBlockedException() {
        super(BLOCKED_JWT_REFRESH_TOKEN);
    }
}
