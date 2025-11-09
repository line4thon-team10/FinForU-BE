package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class NotFoundGuestTokenException extends BaseException {
    public NotFoundGuestTokenException() {
        super(ProductErrorCode.NOT_FOUND_GUEST_TOKEN_403);
    }
}
