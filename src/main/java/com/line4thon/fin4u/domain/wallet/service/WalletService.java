package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;

public interface WalletService {

    // Main Page
    MainWalletRes getWalletMainPage(Long memberId);

    // ADD
    Object addCheckingAccount(Long memberId);
    Object addSavingAccount(Long memberId);
    Object addCard(Long memberId);

    // EDIT
    Object editCheckAccountDetail(Long memberId);
    Object editCardDetail(Long memberId);
    Object editSavingAccountDetail(Long memberId);

    // DELETE
    Void deleteCheckAccount(Long memberId);
    Void deleteSavingAccount(Long memberId);
    Void deleteCard(Long memberId);
}
