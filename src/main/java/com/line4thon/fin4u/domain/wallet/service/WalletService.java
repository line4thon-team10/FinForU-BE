package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.wallet.web.dto.CardReq;
import com.line4thon.fin4u.domain.wallet.web.dto.CheckingAccountReq;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;
import com.line4thon.fin4u.domain.wallet.web.dto.SavingAccountReq;

public interface WalletService {

    // Main Page
    MainWalletRes getWalletMainPage(Long memberId);

    // ADD
    MainWalletRes.CheckingAccounts addCheckingAccount(Long memberId, CheckingAccountReq request);
    MainWalletRes.SavingAccounts addSavingAccount(Long memberId, SavingAccountReq request);
    MainWalletRes.Cards addCard(Long memberId, CardReq request);

    // EDIT
    MainWalletRes.CheckingAccounts editCheckAccountDetail(Long memberId, CheckingAccountReq request);
    MainWalletRes.Cards editCardDetail(Long memberId, CardReq request);
    MainWalletRes.SavingAccounts editSavingAccountDetail(Long memberId, SavingAccountReq request);

    // DELETE
    Void deleteCheckAccount(Long memberId, Long checkingAccountId);
    Void deleteSavingAccount(Long memberId, Long savingAccountId);
    Void deleteCard(Long memberId, Long cardId);
}
