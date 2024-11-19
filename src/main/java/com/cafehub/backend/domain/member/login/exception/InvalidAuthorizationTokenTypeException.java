package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.LoginExceptionCode;

public class InvalidAuthorizationTokenTypeException extends CafeHubException {
    public InvalidAuthorizationTokenTypeException() {
        super(LoginExceptionCode.INVALID_AUTHORIZATION_TOKEN_TYPE);
    }
}
