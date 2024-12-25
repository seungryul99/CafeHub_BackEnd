package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode;

public class InvalidJwtAccessTokenException extends CafeHubException {
    public InvalidJwtAccessTokenException() {
        super(AuthExceptionCode.INVALID_JWT_ACCESS_TOKEN);
    }
}
