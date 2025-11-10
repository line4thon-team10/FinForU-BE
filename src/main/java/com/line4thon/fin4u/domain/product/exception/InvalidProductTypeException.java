package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class InvalidProductTypeException extends BaseException {
    public InvalidProductTypeException() {
        super(ProductErrorCode.INVALID_PRODUCT_TYPE_400);
    }
}
