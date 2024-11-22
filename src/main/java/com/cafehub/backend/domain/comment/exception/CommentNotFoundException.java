package com.cafehub.backend.domain.comment.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.comment.exception.code.CommentExceptionCode.COMMENT_NOT_FOUND;

public class CommentNotFoundException extends CafeHubException {
    public CommentNotFoundException() {
        super(COMMENT_NOT_FOUND);
    }
}
