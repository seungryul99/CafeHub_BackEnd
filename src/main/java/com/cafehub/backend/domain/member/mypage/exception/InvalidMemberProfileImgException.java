package com.cafehub.backend.domain.member.mypage.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.member.mypage.exception.code.MyPageExceptionCode;

public class InvalidMemberProfileImgException extends CafeHubException {
    public InvalidMemberProfileImgException() {
        super(MyPageExceptionCode.INVALID_MEMBER_PROFILE_IMG);
    }
}
