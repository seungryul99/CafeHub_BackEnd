package com.cafehub.backend.common.exception;

import com.cafehub.backend.common.exception.dto.ErrorReason;

public interface BaseErrorCode {

    ErrorReason getErrorReason();
}
