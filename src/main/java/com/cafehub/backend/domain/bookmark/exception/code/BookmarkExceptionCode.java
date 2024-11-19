package com.cafehub.backend.domain.bookmark.exception.code;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum BookmarkExceptionCode implements BaseErrorCode {

    BOOKMARK_ALREADY_EXISTS(BAD_REQUEST, "BOOKMARK_400_1" , "사용자가 이미 해당 카페를 북마크 한 상태 입니다."),
    BOOKMARK_NOT_FOUND(BAD_REQUEST, "BOOKMARK_400_2", "사용자가 해당 카페를 북마크 하지 않은 상태이기 때문에 요청이 거부 되었습니다.")
    ;

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
