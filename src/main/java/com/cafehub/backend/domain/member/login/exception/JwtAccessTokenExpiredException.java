package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode;

public class JwtAccessTokenExpiredException extends CafeHubException {
    public JwtAccessTokenExpiredException() {
        super(AuthExceptionCode.JWT_ACCESS_TOKEN_EXPIRED);
    }
}
