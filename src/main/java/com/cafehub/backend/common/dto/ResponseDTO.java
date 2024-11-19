package com.cafehub.backend.common.dto;


import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.OK;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"success", "code", "errorMessage", "data"})
public class ResponseDTO<T> {

    private final Boolean success;

    private final String code;

    private final String errorMessage;

    private final T data;

    public static <T> ResponseDTO<T> success(T data) {

        return new ResponseDTO<>(true, OK, null, data);
    }

    public static <T> ResponseDTO<T> fail(String code, String errorMessage) {

        return new ResponseDTO<>(false , code , errorMessage, null);
    }

    public static <T> ResponseDTO<T> fail(ErrorReason errorReason) {

        return new ResponseDTO<>(false , errorReason.getCode() , errorReason.getErrorMessage(), null);
    }


}