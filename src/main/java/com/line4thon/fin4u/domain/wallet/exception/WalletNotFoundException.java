package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class WalletNotFoundException extends BaseException {
    public WalletNotFoundException() {
        super(WalletErrorCode.WALLET_NOT_FOUND);
    }
}
