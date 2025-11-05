package com.line4thon.fin4u.domain.exrate.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class KoreanBankApuResponseNullException extends BaseException {
    public KoreanBankApuResponseNullException() {
        super(ExchangeRateErrorCode.KOREAN_BANK_API_RESPONSE_NULL);
    }
}
