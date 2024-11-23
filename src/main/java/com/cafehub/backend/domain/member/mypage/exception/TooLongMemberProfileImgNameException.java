package com.cafehub.backend.domain.member.mypage.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.mypage.exception.code.MyPageExceptionCode.TOO_LONG_MEMBER_PROFILE_IMG_NAME;

public class TooLongMemberProfileImgNameException extends CafeHubException {
    public TooLongMemberProfileImgNameException() {
        super(TOO_LONG_MEMBER_PROFILE_IMG_NAME);
    }
}
