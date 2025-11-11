package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class NotFoundSavingException extends BaseException {
    public NotFoundSavingException() {
        super(ProductErrorCode.NOT_FOUND_SAVING_404);
    }
}
