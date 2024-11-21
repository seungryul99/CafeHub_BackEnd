package com.cafehub.backend.domain.member.mypage.exception;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.mypage.exception.code.MyPageExceptionCode;

public class MemberNicknameTooShortException extends CafeHubException {
    public MemberNicknameTooShortException() {
        super(MyPageExceptionCode.MEMBER_NICKNAME_TOO_SHORT);
    }
}
