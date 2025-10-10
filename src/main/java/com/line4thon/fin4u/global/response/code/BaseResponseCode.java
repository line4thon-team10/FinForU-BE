package com.line4thon.fin4u.global.response.code;

// 공통 응답에 대한 interface
public interface BaseResponseCode {
    String getCode();
    int getHttpStatus();
    String getMessage();
}
