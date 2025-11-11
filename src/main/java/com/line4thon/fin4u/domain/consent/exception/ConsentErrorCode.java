package com.line4thon.fin4u.domain.consent.exception;


import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//오류 메시지를 한 곳에서 관리
@Getter
@RequiredArgsConstructor
public enum ConsentErrorCode implements BaseResponseCode {
    ESSENTIAL_CONSENT(400, "CONSENT-001", "필수 권한을 모두 동의해야 합니다");
    private final int httpStatus;
    private final String code;
    private final String message;
}