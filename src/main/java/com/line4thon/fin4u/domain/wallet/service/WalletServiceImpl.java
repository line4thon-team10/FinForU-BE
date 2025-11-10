package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.repository.CardRepository;
import com.line4thon.fin4u.domain.wallet.repository.CheckingAccountRepository;
import com.line4thon.fin4u.domain.wallet.repository.SavingAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WallerService{

    private final MemberRepository memberRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final CheckingAccountRepository checkingAccountRepository;
    private final CardRepository cardRepository;
}
