package com.cafehub.backend.domain.member.mypage.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.member.mypage.exception.code.MyPageExceptionCode.INVALID_MEMBER_PROFILE_IMG_EXTENSION;

public class InvalidMemberProfileImgExtensionException extends CafeHubException {
    public InvalidMemberProfileImgExtensionException() {
        super(INVALID_MEMBER_PROFILE_IMG_EXTENSION);
    }
}
