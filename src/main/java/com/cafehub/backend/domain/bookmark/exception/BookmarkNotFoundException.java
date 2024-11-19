package com.cafehub.backend.domain.bookmark.exception;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.bookmark.exception.code.BookmarkExceptionCode;

public class BookmarkNotFoundException extends CafeHubException {
    public BookmarkNotFoundException() {
        super(BookmarkExceptionCode.BOOKMARK_NOT_FOUND);
    }
}
