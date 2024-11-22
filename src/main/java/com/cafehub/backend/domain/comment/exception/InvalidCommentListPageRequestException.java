package com.cafehub.backend.domain.comment.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.comment.exception.code.CommentExceptionCode.COMMENT_LIST_INVALID_PAGE_REQUEST;

public class InvalidCommentListPageRequestException extends CafeHubException {
    public InvalidCommentListPageRequestException() {
        super(COMMENT_LIST_INVALID_PAGE_REQUEST);
    }
}
