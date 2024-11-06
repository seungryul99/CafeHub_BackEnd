package com.cafehub.backend.common.constants;

public class CafeHubConstants {

    // 모든 API Success Code 통일
    public static final String OK = "200 OK";


    // HTTP ErrorCode
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    public static final int METHOD_NOT_ALLOWED = 405;

    public static final int INTERNAL_SERVER_ERROR = 500;


    // Top N Review Paging size
    public static final int TOP_N_REVIEW_SIZE = 2;

    // CafeList Paging size

    public static final int CAFE_LIST_PAGING_SIZE = 10;
}