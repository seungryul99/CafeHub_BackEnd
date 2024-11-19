package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.LoginExceptionCode;

public class InvalidJwtAccessTokenException extends CafeHubException {
    public InvalidJwtAccessTokenException() {
        super(LoginExceptionCode.INVALID_JWT_ACCESS_TOKEN);
    }
}
