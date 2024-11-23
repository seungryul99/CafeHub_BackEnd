package com.cafehub.backend.domain.comment.exception.code;

import com.cafehub.backend.common.constants.CafeHubConstants;
import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cafehub.backend.common.constants.CafeHubConstants.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum CommentExceptionCode implements BaseErrorCode {

    COMMENT_LIST_INVALID_PAGE_REQUEST(BAD_REQUEST, "COMMENT_400_1", "마지막 페이지를 초과한 요청입니다, 유효한 페이지 번호를 입력해 주세요."),
    COMMENT_NOT_FOUND(BAD_REQUEST, "COMMENT_400_2", "해당 commentId에 대한 댓글이 존재하지 않습니다.")
    ;

    private final Integer status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
