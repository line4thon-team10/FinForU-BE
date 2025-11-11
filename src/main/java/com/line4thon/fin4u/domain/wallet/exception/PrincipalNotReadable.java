package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class PrincipalNotReadable extends BaseException {
    public PrincipalNotReadable() {
        super(WalletErrorCode.PRINCIPAL_NOT_READABLE);
    }
}
