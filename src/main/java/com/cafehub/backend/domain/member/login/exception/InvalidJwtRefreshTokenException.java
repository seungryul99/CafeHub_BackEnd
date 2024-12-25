package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode.INVALID_JWT_REFRESH_TOKEN;

public class InvalidJwtRefreshTokenException extends CafeHubException {
    public InvalidJwtRefreshTokenException() {
        super(INVALID_JWT_REFRESH_TOKEN);
    }
}
