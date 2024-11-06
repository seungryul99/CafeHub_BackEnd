package com.cafehub.backend.common.exception;

import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CafeHubException extends RuntimeException{

    private final BaseErrorCode code;

    public ErrorReason getErrorReason(){
        return this.code.getErrorReason();
    }
}