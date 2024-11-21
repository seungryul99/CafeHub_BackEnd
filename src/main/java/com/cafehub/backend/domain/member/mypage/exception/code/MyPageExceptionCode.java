package com.cafehub.backend.domain.member.mypage.exception.code;

import com.cafehub.backend.common.constants.CafeHubConstants;
import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum MyPageExceptionCode implements BaseErrorCode {

    MEMBER_NICKNAME_DUPLICATE(BAD_REQUEST, "MYPAGE_400_1", "변경하려는 닉네임이 이미 존재합니다."),
    MEMBER_NICKNAME_TOO_SHORT(BAD_REQUEST, "MYPAGE_400_2", "닉네임은 앞뒤로 공백이 허용되지 않고 3글자 이상이어야 합니다."),
    INVALID_MEMBER_PROFILE_IMG(BAD_REQUEST, "MYPAGE_400_3", "프로필 이미지는 png, jpg, jpeg 파일만 가능합니다.")
    ;


    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
