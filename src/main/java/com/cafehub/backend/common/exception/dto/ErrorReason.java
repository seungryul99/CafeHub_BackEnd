package com.cafehub.backend.common.exception.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorReason {

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Builder(access = AccessLevel.PRIVATE)
    private ErrorReason(Integer status, String code, String errorMessage) {
        this.status = status;
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public static ErrorReason of(Integer status, String code, String errorMessage){
        return ErrorReason.builder()
                .status(status)
                .code(code)
                .errorMessage(errorMessage)
                .build();
    }
}
