package com.cafehub.backend.domain.review.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.review.exception.code.ReviewExceptionCode.REVIEW_LIST_INVALID_PAGE_REQUEST;

public class InvalidReviewListPageRequestException extends CafeHubException {
    public InvalidReviewListPageRequestException() {
        super(REVIEW_LIST_INVALID_PAGE_REQUEST);
    }
}
