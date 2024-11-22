package com.cafehub.backend.domain.review.exception;

import com.cafehub.backend.common.exception.CafeHubException;

import static com.cafehub.backend.domain.review.exception.code.ReviewExceptionCode.INVALID_REVIEW_PHOTO_EXTENSION;

public class InvalidReviewPhotoExtensionException extends CafeHubException {
    public InvalidReviewPhotoExtensionException() {
        super(INVALID_REVIEW_PHOTO_EXTENSION);
    }
}
