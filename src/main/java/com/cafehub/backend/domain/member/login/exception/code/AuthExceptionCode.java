package com.cafehub.backend.domain.member.login.exception.code;


import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Getter
@AllArgsConstructor
public enum AuthExceptionCode implements BaseErrorCode {

    AUTHORIZATION_HEADER_VALUE_NOT_FOUND(BAD_REQUEST, "LOGIN_400_1", "Authorization 헤더에 아무 값이 들어있지 않습니다. 인증 헤더를 다시 확인해 주세요."),
    INVALID_AUTHORIZATION_TOKEN_TYPE(BAD_REQUEST, "LOGIN_400_2", "Authorization 헤더에 들어있는 토큰의 타입이 \"Bearer \" 가 아닙니다. 토큰 타입을 다시 확인해 주세요."),
    MEMBER_NOT_FOUND(BAD_REQUEST, "LOGIN_400_3", "Jwt 토큰으로 부터 추출한 Id 에 해당하는 Member가 데이터 베이스에 존재하지 않습니다."),
    JWT_REFRESH_TOKEN_NOT_EXIT(BAD_REQUEST, "LOGIN_400_4", "토큰 재발행을 위한 JwtRefreshToken이 요청에 포함되지 않았습니다."),

    JWT_ACCESS_TOKEN_EXPIRED(UNAUTHORIZED,"LOGIN_401_1","JWT Access Token이 만료 되었습니다."),
    INVALID_JWT_ACCESS_TOKEN(UNAUTHORIZED, "LOGIN_401_2", "유효하지 않은 JWT Access Token 입니다."),
    JWT_REFRESH_TOKEN_EXPIRED(UNAUTHORIZED,"LOGIN_401_3","JWT Refresh Token이 만료 되었습니다."),
    INVALID_JWT_REFRESH_TOKEN(UNAUTHORIZED,"LOGIN_401_4","유효하지 않은 JWT Refresh Token 입니다."),
    BLOCKED_JWT_REFRESH_TOKEN(UNAUTHORIZED, "LOGIN_401_5", "차단된 JWT Refresh Token 입니다."),

    AUTHORIZATION_EXCEPTION(FORBIDDEN, "LOGIN_403_1", "해당 리소스에 접근 할 수 있는 권한이 없습니다.");


    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
