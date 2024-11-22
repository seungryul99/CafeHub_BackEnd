package com.cafehub.backend.domain.review.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.review.exception.code.ReviewExceptionCode.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends CafeHubException {
    public ReviewNotFoundException() {
        super(REVIEW_NOT_FOUND);
    }
}
