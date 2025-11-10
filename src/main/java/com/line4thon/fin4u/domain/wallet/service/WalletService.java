package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.wallet.web.dto.CardReq;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;

public interface WalletService {

    // Main Page
    MainWalletRes getWalletMainPage(Long memberId);

    // ADD
    Void addCheckingAccount(Long memberId);
    Void addSavingAccount(Long memberId);
    MainWalletRes.Cards addCard(Long memberId, CardReq request);

    // EDIT
    Void editCheckAccountDetail(Long memberId);
    MainWalletRes.Cards editCardDetail(Long memberId, CardReq request);
    Void editSavingAccountDetail(Long memberId);

    // DELETE
    Void deleteCheckAccount(Long memberId);
    Void deleteSavingAccount(Long memberId);
    Void deleteCard(Long memberId);
}
