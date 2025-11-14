package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class CardNotFoundException extends BaseException {
  public CardNotFoundException() {
    super(WalletErrorCode.CARD_NOT_FOUND);
  }
}
