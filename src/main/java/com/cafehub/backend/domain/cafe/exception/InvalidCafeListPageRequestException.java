package com.cafehub.backend.domain.cafe.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.cafe.exception.code.CafeExceptionCode;

public class InvalidCafeListPageRequestException extends CafeHubException {

    public InvalidCafeListPageRequestException() {
        super(CafeExceptionCode.INVALID_PAGE_REQUEST);
    }
}