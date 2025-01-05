package com.cafehub.backend.domain.member.login.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.login.exception.code.AuthExceptionCode.MEMBER_NOT_FOUND;

public class MemberNotFoundException extends CafeHubException {
    public MemberNotFoundException() {
        super(MEMBER_NOT_FOUND);
    }
}
