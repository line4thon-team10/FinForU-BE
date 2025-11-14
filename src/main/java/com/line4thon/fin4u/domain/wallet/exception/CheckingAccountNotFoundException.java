package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class CheckingAccountNotFoundException extends BaseException {
  public CheckingAccountNotFoundException() {
    super(WalletErrorCode.CHECKING_ACCOUNT_NOT_FOUND);
  }
}
