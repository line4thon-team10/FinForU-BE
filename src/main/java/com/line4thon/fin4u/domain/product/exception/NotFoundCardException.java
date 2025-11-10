package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class NotFoundCardException extends BaseException {
    public NotFoundCardException() {
        super(ProductErrorCode.NOT_FOUND_CARD_404);
    }
}
