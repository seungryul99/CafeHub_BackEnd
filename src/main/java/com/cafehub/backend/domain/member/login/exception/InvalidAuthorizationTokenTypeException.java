package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode;

public class InvalidAuthorizationTokenTypeException extends CafeHubException {
    public InvalidAuthorizationTokenTypeException() {
        super(AuthExceptionCode.INVALID_AUTHORIZATION_TOKEN_TYPE);
    }
}
