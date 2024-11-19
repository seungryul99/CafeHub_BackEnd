package com.cafehub.backend.domain.cafe.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.cafe.exception.code.CafeExceptionCode;

public class CafeNotFoundException extends CafeHubException {
    public CafeNotFoundException() {
        super(CafeExceptionCode.CAFE_NOT_FOUND);
    }
}
