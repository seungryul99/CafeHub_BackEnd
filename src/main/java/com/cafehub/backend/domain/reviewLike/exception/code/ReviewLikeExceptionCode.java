package com.cafehub.backend.domain.reviewLike.exception.code;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ReviewLikeExceptionCode implements BaseErrorCode {

    REVIEW_LIKE_ALREADY_EXIST(BAD_REQUEST, "REVIEW_LIKE_400_1", "이미 해당 리뷰에 대한 좋아요가 존재합니다."),
    REVIEW_LIKE_NOT_FOUND(BAD_REQUEST, "REVIEW_LIKE_400_2", "해당 리뷰에 대한 좋아요가 존재하지 않습니다.")
    ;


    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
