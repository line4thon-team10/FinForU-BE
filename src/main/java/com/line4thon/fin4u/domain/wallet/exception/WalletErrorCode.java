package com.line4thon.fin4u.domain.wallet.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletErrorCode implements BaseResponseCode {

    PRINCIPAL_NOT_READABLE("PRINCIPAL_NOT_READABLE", 401, "jwt를 읽을 수 없습니다."),
    WALLET_NOT_FOUND("WALLET_NOT_FOUND_404", 404, "해당 멤버가 소지하고 있는 지갑이 없습니다."),
    CARD_NOT_FOUND("CARD_NOT_FOUND_404", 404, "해당 카드를 찾을 수 없습니다."),
    CHECKING_ACCOUNT_NOT_FOUND("CHECKING_ACCOUNT_NOT_FOUND_404", 404, "해당 예금 계좌를 찾을 수 없습니다."),
    SAVING_ACCOUNT_NOT_FOUND("SAVING_ACCOUNT_NOT_FOUND_404", 404, "해당 저축 계좌를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
