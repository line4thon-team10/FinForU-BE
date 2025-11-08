package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements BaseResponseCode {
    ;

    private final String code;
    private final int httpStatus;
    private final String message;
}
