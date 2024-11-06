package com.cafehub.backend.common.exception.global;

import com.cafehub.backend.common.constants.CafeHubConstants;
import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;


@Getter
@AllArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {
    // 아직 예시 단계
    _BAD_REQUEST(BAD_REQUEST, "COMMON 400", "사용자의 잘못된 요청입니다."),
    _INVALID_PARAM_REQUEST(BAD_REQUEST, "COMMON 400_1", "파라미터를 다시 확인해 주세요"),
    _PARAM_TYPE_MISMATCH_REQUEST(BAD_REQUEST, "COMMON 400_2", "파라미터의 타입을 확인해 주세요"),

    _UNAUTHORIZED(UNAUTHORIZED, "COMMON 401", "인증되지 않은 사용자입니다"),

    _FORBIDDEN(FORBIDDEN, "COMMON 403", "사용자에게 접근 권한이 없습니다"),

    _NOT_FOUND(NOT_FOUND, "COMMON 404", "해당 경로로 가능한 요청이 없습니다"),

    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "COMMON 405", "허용되지 않은 HTTP 메서드로 요청이 발생했습니다"),
    _UNKNOWN_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON 500", "서버 내부 에러가 발생했습니다"),
    ;
    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
