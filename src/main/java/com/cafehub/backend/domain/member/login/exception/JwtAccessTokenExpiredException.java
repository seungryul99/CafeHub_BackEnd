package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.LoginExceptionCode;

public class JwtAccessTokenExpiredException extends CafeHubException {
    public JwtAccessTokenExpiredException() {
        super(LoginExceptionCode.JWT_ACCESS_TOKEN_EXPIRED);
    }
}
