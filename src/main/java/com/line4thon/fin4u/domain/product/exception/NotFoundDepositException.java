package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class NotFoundDepositException extends BaseException {
    public NotFoundDepositException() {
        super(ProductErrorCode.NOT_FOUND_DEPOSIT_404);
    }
}
