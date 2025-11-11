package com.line4thon.fin4u.domain.wallet.service;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.entity.CheckingAccount;
import com.line4thon.fin4u.domain.wallet.entity.SavingAccount;
import com.line4thon.fin4u.domain.wallet.entity.Wallet;
import com.line4thon.fin4u.domain.wallet.entity.WalletCard;
import com.line4thon.fin4u.domain.wallet.exception.WalletNotFoundException;
import com.line4thon.fin4u.domain.wallet.repository.WalletCardRepository;
import com.line4thon.fin4u.domain.wallet.repository.CheckingAccountRepository;
import com.line4thon.fin4u.domain.wallet.repository.SavingAccountRepository;
import com.line4thon.fin4u.domain.wallet.repository.WalletRepository;
import com.line4thon.fin4u.domain.wallet.web.dto.CardReq;
import com.line4thon.fin4u.domain.wallet.web.dto.CheckingAccountReq;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;
import com.line4thon.fin4u.domain.wallet.web.dto.SavingAccountReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final CheckingAccountRepository checkingAccountRepository;
    private final WalletCardRepository cardRepository;

    @Override
    public MainWalletRes getWalletMainPage(Long memberId) {
        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseThrow(WalletNotFoundException::new);

        List<MainWalletRes.CheckingAccounts> checks = wallet.getCheckingAccounts().stream()
                .map(c -> new MainWalletRes.CheckingAccounts(
                        c.getId(),
                        c.getBank().getLower()
                ))
                .toList();

        List<MainWalletRes.SavingAccounts> saves = wallet.getSavingAccounts().stream()
                .map(s -> new MainWalletRes.SavingAccounts(
                        s.getId(),
                        s.getSavingName()
                ))
                .toList();

        List<MainWalletRes.Cards> cards = wallet.getCards().stream()
                .map(c -> new MainWalletRes.Cards(
                        c.getId(),
                        c.getCardName()
                ))
                .toList();

        return new MainWalletRes(
                wallet.getId(),
                checks,
                saves,
                cards
        );
    }

    @Override
    @Transactional
    public MainWalletRes.CheckingAccounts addCheckingAccount(Long memberId, CheckingAccountReq request) {
        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseGet(() ->
                        walletRepository.save(Wallet.builder()
                                .member(memberRepository.findByMemberId(memberId)
                                        .orElseThrow(MemberNotFoundException::new))
                                .build())
                );

        CheckingAccount checkingAccount = CheckingAccount.builder()
                .wallet(wallet)
                .bank(request.getBank())
                .build();
        List<CheckingAccount> accounts = wallet.getCheckingAccounts();
        if(accounts == null) {
            accounts = new ArrayList<>();
        }
        accounts.add(checkingAccount);
        checkingAccountRepository.save(checkingAccount);
        return new MainWalletRes.CheckingAccounts(
                checkingAccount.getId(),
                checkingAccount.getBank().getLower()
        );
    }

    @Override
    @Transactional
    public MainWalletRes.SavingAccounts addSavingAccount(Long memberId, SavingAccountReq request) {
        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseGet(() -> walletRepository.save(
                        new Wallet().builder()
                                .member(memberRepository.findByMemberId(memberId)
                                        .orElseThrow(MemberNotFoundException::new))
                                .build()
                ));

        List<SavingAccount> savingAccounts = wallet.getSavingAccounts();
        if(savingAccounts == null) {
            savingAccounts = new ArrayList<>();
        }

        SavingAccount account = SavingAccount.builder()
                .wallet(wallet)
                .bank(request.getBank())
                .savingType(request.getProductType())
                .savingName(request.getProductName())
                .monthlyPay(request.getMonthlyPayment())
                .paymentDate(request.getUpcomingDate())
                .startDate(request.getStart())
                .endDate(request.getEnd())
                .build();
        savingAccounts.add(account);
        savingAccountRepository.save(account);
        return new MainWalletRes.SavingAccounts(
                account.getId(),
                account.getSavingName()
        );
    }

    @Override
    @Transactional
    public MainWalletRes.Cards addCard(Long memberId, CardReq request) {

        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseGet(() ->
                        walletRepository.save(
                                Wallet.builder()
                                        .member(memberRepository.findByMemberId(memberId)
                                                .orElseThrow(MemberNotFoundException::new))
                                        .build()
                        )
                );
        WalletCard card = WalletCard.builder()
                .bank(request.getBank())
                .wallet(wallet)
                .cardType(request.getCardType())
                .cardName(request.getCardName())
                .paymentDate(request.getUpcomingDate() == null ? null : request.getUpcomingDate().getDayOfMonth())
                .build();
        cardRepository.save(card);

        return new MainWalletRes.Cards(
                card.getId(),
                card.getCardName()
        );
    }

    @Override
    @Transactional
    public MainWalletRes.CheckingAccounts editCheckAccountDetail(Long memberId, CheckingAccountReq request) {
        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Map<Long, CheckingAccount> accountMap = wallet.getCheckingAccounts().stream()
                .collect(Collectors.toMap(CheckingAccount::getId, c -> c));

        CheckingAccount found = accountMap.get(request.getCheckingAccountId());

        found.modify(request.getBank());
        return new MainWalletRes.CheckingAccounts(
                found.getId(),
                found.getBank().getLower()
        );
    }

    @Override
    @Transactional
    public MainWalletRes.Cards editCardDetail(Long memberId, CardReq request) {
        List<WalletCard> cards = walletRepository.findByMemberMemberId(memberId)
                .orElseThrow(WalletNotFoundException::new)
                .getCards();
        Map<Long, WalletCard> cardMap = cards.stream().collect(Collectors.toMap(WalletCard::getId, c -> c));
        WalletCard foundCard = cardMap.get(request.getCardId());

        foundCard.modify()
                .cardType(request.getCardType())
                .bank(request.getBank())
                .cardName(request.getCardName())
                .paymentDate(request.getUpcomingDate().getDayOfMonth())
                .build();

        return new MainWalletRes.Cards(
                foundCard.getId(),
                foundCard.getCardName()
        );
    }

    @Override
    @Transactional
    public MainWalletRes.SavingAccounts editSavingAccountDetail(Long memberId, SavingAccountReq request) {
        Wallet wallet = walletRepository.findByMemberMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Map<Long, SavingAccount> accountMap = wallet.getSavingAccounts().stream()
                .collect(Collectors.toMap(SavingAccount::getId, s -> s));

        SavingAccount found = accountMap.get(request.getSavingAccountId());

        found.modify(
                request.getBank(),
                request.getProductType(),
                request.getProductName(),
                request.getStart(),
                request.getEnd(),
                request.getMonthlyPayment(),
                request.getUpcomingDate());
        return new MainWalletRes.SavingAccounts(
                found.getId(),
                found.getSavingName()
        );
    }

    @Override
    @Transactional
    public Void deleteCheckAccount(Long memberId, Long checkingAccountId) {
        checkingAccountRepository.deleteById(checkingAccountId);
        return null;
    }

    @Override
    @Transactional
    public Void deleteSavingAccount(Long memberId, Long savingAccountId) {
        savingAccountRepository.deleteById(savingAccountId);
        return null;
    }

    @Override
    @Transactional
    public Void deleteCard(Long memberId, Long cardId) {
        cardRepository.deleteById(cardId);
        return null;
    }
}
