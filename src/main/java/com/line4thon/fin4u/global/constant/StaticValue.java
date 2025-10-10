package com.line4thon.fin4u.global.constant;

// 공통 응답
public class StaticValue {
    // 2XX case
    public static final int OK = 200;
    public static final int CREATED = 201;

    // 4XX case
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int CONFLICT = 409;

    // 5XX case
    public static final int INTERNAL_SERVER_ERROR = 500;
}
