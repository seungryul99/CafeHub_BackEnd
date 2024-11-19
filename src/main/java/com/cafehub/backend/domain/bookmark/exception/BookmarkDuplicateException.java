package com.cafehub.backend.domain.bookmark.exception;

import com.cafehub.backend.common.exception.BaseErrorCode;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.bookmark.exception.code.BookmarkExceptionCode;

public class BookmarkDuplicateException extends CafeHubException {
    public BookmarkDuplicateException() {
        super(BookmarkExceptionCode.BOOKMARK_ALREADY_EXISTS);
    }
}
