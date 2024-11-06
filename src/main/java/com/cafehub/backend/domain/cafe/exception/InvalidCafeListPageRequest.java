package com.cafehub.backend.domain.cafe.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.cafe.exception.code.CafeExceptionCode;

public class InvalidCafeListPageRequest extends CafeHubException {

    public InvalidCafeListPageRequest() {
        super(CafeExceptionCode.INVALID_PAGE_REQUEST);
    }
}