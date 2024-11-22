package com.cafehub.backend.domain.reviewLike.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.reviewLike.exception.code.ReviewLikeExceptionCode.REVIEW_LIKE_ALREADY_EXIST;

public class ReviewLikeAlreadyExistException extends CafeHubException {
    public ReviewLikeAlreadyExistException() {
        super(REVIEW_LIKE_ALREADY_EXIST);
    }
}
