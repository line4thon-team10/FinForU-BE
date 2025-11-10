package com.line4thon.fin4u.domain.wallet.web.controller;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.service.WalletService;
import com.line4thon.fin4u.domain.wallet.web.dto.CardReq;
import com.line4thon.fin4u.domain.wallet.web.dto.CheckingAccountReq;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final MemberRepository memberRepository;

    private Long getMemberId(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new).getMemberId();
    }

    @GetMapping("/main")
    public ResponseEntity<SuccessResponse<?>> getWalletMainPage(
            Principal principal
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) {
            throw new RuntimeException("인증 안 됨");
        }

        MainWalletRes response = walletService.getWalletMainPage(getMemberId(principal.getName()));

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PostMapping("/card")
    public ResponseEntity<SuccessResponse<?>> addCard(
            Principal principal,
            @RequestBody @Validated CardReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) {
            throw new RuntimeException("인증 안 됨");
        }
        MainWalletRes.Cards response = walletService.addCard(getMemberId(principal.getName()), request);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PostMapping("/check-account")
    public ResponseEntity<SuccessResponse<?>> addCheckingAccount(
            Principal principal,
            @RequestBody @Validated CheckingAccountReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank())
            throw new RuntimeException("인증 안 됨");
        MainWalletRes.CheckingAccounts response = walletService.addCheckingAccount(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PutMapping("/card")
    public ResponseEntity<SuccessResponse<?>> modifyCardDetail(
            Principal principal,
            @RequestBody @Validated CardReq request
    ) {
        if (principal.getName().isEmpty() || principal.getName().isBlank()) {
            throw new RuntimeException("인증 안 됨");
        }
        MainWalletRes.Cards response = walletService.editCardDetail(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));

    }

    @PutMapping("/check-account")
    public ResponseEntity<SuccessResponse<?>> modifyCheckingAccount(
            Principal principal,
            @RequestBody @Validated CheckingAccountReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank())
            throw new RuntimeException("인증 안 됨");
        MainWalletRes.CheckingAccounts response = walletService.editCheckAccountDetail(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }
}
