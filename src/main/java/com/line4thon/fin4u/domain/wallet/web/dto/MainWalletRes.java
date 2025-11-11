package com.line4thon.fin4u.domain.wallet.web.dto;

import java.util.List;

public record MainWalletRes(
        Long walletId,
        List<CheckingAccounts> checkAccount,
        List<SavingAccounts> saveAccount,
        List<Cards> cards
) {
    public record CheckingAccounts(
            Long checkingId,
            String bankName
    ) {}

    public record SavingAccounts(
            Long savingId,
            String name
    ) {}
    public record Cards(
            Long cardId,
            String name
    ) {}
}
