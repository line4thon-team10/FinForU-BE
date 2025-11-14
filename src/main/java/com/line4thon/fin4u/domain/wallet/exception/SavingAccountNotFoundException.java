package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class SavingAccountNotFoundException extends BaseException {
    public SavingAccountNotFoundException() {
        super(WalletErrorCode.SAVING_ACCOUNT_NOT_FOUND);
    }
}
