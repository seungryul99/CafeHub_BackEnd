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
    _BAD_REQUEST(BAD_REQUEST, "COMMON_400", "사용자의 잘못된 요청입니다."),
    _INVALID_PARAM_REQUEST(BAD_REQUEST, "COMMON_400_1", "파라미터를 다시 확인해 주세요"),
    _PARAM_TYPE_MISMATCH_REQUEST(BAD_REQUEST, "COMMON_400_2", "파라미터의 타입을 확인해 주세요"),

    _UNAUTHORIZED(UNAUTHORIZED, "COMMON_401", "인증되지 않은 사용자입니다"),

    _FORBIDDEN(FORBIDDEN, "COMMON_403", "사용자에게 접근 권한이 없습니다"),

    _NOT_FOUND(NOT_FOUND, "COMMON_404", "해당 경로로 가능한 요청이 없습니다"),

    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "COMMON_405", "허용되지 않은 HTTP 메서드로 요청이 발생했습니다"),
    _UNKNOWN_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON 500_1", "서버 내부에서 알수 없는 에러가 발생했습니다. 관리자에게 문의해 주세요."),
    _REST_CLIENT_ERROR(INTERNAL_SERVER_ERROR, "COMMON_500_2", "RestClient 사용한 CafeHub -> OAuth Resource Server 통신 중 예외가 발생했습니다. 관리자에게 문의해 주세요."),
    _S3_FILE_UPLOAD_ERROR(INTERNAL_SERVER_ERROR, "COMMON_500_3", "S3 저장소에 이미지 저장중 에러가 발생했습니다. 관리자에게 문의해 주세요.")
    ;
    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
