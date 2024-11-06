package com.cafehub.backend.domain.cafe.exception.code;

import com.cafehub.backend.common.constants.CafeHubConstants;
import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Getter
@AllArgsConstructor
public enum CafeExceptionCode implements BaseErrorCode {

    INVALID_PAGE_REQUEST(BAD_REQUEST, "CAFE 400_1", "마지막 페이지를 초과한 요청입니다, 유효한 페이지 번호를 입력해 주세요");

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
