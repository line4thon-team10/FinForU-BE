package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.repository.CardRepository;
import com.line4thon.fin4u.domain.wallet.repository.CheckingAccountRepository;
import com.line4thon.fin4u.domain.wallet.repository.SavingAccountRepository;
import com.line4thon.fin4u.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WallerService{

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final CheckingAccountRepository checkingAccountRepository;
    private final CardRepository cardRepository;

    @Override
    public Object getWalletMainPage(Long memberId) {

        return null;
    }

    @Override
    @Transactional
    public Object addCheckingAccount(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Object addSavingAccount(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Object addCard(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Object editCheckAccountDetail(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Object editCardDetail(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Object editSavingAccountDetail(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Void deleteCheckAccount(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Void deleteSavingAccount(Long memberId) {
        return null;
    }

    @Override
    @Transactional
    public Void deleteCard(Long memberId) {
        return null;
    }
}
