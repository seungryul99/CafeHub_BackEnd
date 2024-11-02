package com.cafehub.backend.common.exception.global;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;


@Getter
@AllArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {

    // 아직 예시 단계
    UNKNOWN_BAD_REQUEST(BAD_REQUEST, "COMMON 400", "사용자의 잘못된 요청입니다."),
    UNKNOWN_UNAUTHORIZED(UNAUTHORIZED, "COMMON 401", "인증되지 않은 사용자입니다."),
    UNKNOWN_FORBIDDEN(FORBIDDEN, "COMMON 403", "사용자에게 접근 권한이 없습니다."),
    UNKNOWN_INTERNAL_SERVER(INTERNAL_SERVER_ERROR, "COMMON 500", "서버 내부 에러가 발생했습니다.");

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
