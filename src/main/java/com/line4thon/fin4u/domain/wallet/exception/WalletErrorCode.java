package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletErrorCode implements BaseResponseCode {

    WALLET_NOT_FOUND("WALLET_NOT_FOUND_404", 404, "해당 멤버가 소지하고 있는 지갑이 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
