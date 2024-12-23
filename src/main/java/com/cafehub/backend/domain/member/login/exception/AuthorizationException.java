package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.LoginExceptionCode;

public class AuthorizationException extends CafeHubException {
    public AuthorizationException() {
        super(LoginExceptionCode.AUTHORIZATION_EXCEPTION);
    }
}
