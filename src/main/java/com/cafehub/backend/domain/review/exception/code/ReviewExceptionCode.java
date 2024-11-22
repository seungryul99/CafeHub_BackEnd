package com.cafehub.backend.domain.review.exception.code;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum ReviewExceptionCode  implements BaseErrorCode {

    REVIEW_LIST_INVALID_PAGE_REQUEST(BAD_REQUEST, "REVIEW_400_1", "마지막 페이지를 초과한 요청입니다, 유효한 페이지 번호를 입력해 주세요."),
    TOO_MANY_REVIEW_PHOTOS_REQUEST(BAD_REQUEST, "REVIEW_400_2", "리뷰 사진은 5장이상 첨부할 수 없습니다."),
    TOO_LONG_REVIEW_PHOTO_NAME(BAD_REQUEST, "REVIEW_400_3", "리뷰 사진의 파일 명은 50자를 넘을 수 없습니다."),
    INVALID_REVIEW_PHOTO_EXTENSION(BAD_REQUEST, "REVIEW_400_4", "리뷰 사진의 확장자는 png, jpg, jpeg만 가능합니다."),
    REVIEW_NOT_FOUND(BAD_REQUEST, "REVIEW_400_5", "리뷰 ID에 해당하는 리뷰가 존재하지 않습니다. reviewId 파라미터를 다시 확인해 주세요.")
    ;


    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
