package com.cafehub.backend.domain.review.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.review.exception.code.ReviewExceptionCode.TOO_LONG_REVIEW_PHOTO_NAME;

public class TooLongReviewPhotoNameException extends CafeHubException {
    public TooLongReviewPhotoNameException() {
        super(TOO_LONG_REVIEW_PHOTO_NAME);
    }
}
