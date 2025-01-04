package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode;

public class JwtRefreshTokenNotExistException extends CafeHubException {
    public JwtRefreshTokenNotExistException() {
        super(AuthExceptionCode.JWT_REFRESH_TOKEN_NOT_EXIT);
    }
}
