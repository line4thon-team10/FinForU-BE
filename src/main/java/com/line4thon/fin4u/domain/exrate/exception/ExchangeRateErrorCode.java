package com.line4thon.fin4u.domain.exrate.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeRateErrorCode implements BaseResponseCode {

    KOREAN_BANK_API_RESPONSE_NULL("KOREAN_BANK_API_500", 500, "한국 은행 API 호출에 문제가 발생했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
