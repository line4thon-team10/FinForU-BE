package com.line4thon.fin4u.domain.specialize.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class SpecializeBankNotFoundException extends BaseException {
    public SpecializeBankNotFoundException() {
        super(SpecializeBankErrorCode.SPECIALIZE_BANK_NOT_FOUND);
    }
}
