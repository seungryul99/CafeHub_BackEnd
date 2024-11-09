package com.cafehub.backend.domain.menu.exception.code;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum MenuExceptionCode implements BaseErrorCode {

    MENU_LIST_NOT_FOUND(BAD_REQUEST, "MENU_400_1", "요청한 카페의 ID에 해당하는 카페가 존재하지 않거나 아직 메뉴가 등록되지 않은 카페 일 수 있습니다. cafeId 파라미터를 다시 확인해 주세요.")
    ;

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
