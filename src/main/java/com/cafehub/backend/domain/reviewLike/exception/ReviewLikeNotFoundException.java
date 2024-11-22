package com.cafehub.backend.domain.reviewLike.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.reviewLike.exception.code.ReviewLikeExceptionCode.REVIEW_LIKE_NOT_FOUND;

public class ReviewLikeNotFoundException extends CafeHubException {
    public ReviewLikeNotFoundException() {
        super(REVIEW_LIKE_NOT_FOUND);
    }
}
