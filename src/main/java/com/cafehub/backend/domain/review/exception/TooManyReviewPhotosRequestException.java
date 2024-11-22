package com.cafehub.backend.domain.review.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.review.exception.code.ReviewExceptionCode.TOO_MANY_REVIEW_PHOTOS_REQUEST;

public class TooManyReviewPhotosRequestException extends CafeHubException {
    public TooManyReviewPhotosRequestException() {
        super(TOO_MANY_REVIEW_PHOTOS_REQUEST);
    }
}
