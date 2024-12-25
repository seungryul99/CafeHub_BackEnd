package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode;


public class AuthorizationHeaderNotExistException extends CafeHubException {
    public AuthorizationHeaderNotExistException() {
        super(AuthExceptionCode.AUTHORIZATION_HEADER_VALUE_NOT_FOUND);
    }
}
