package com.line4thon.fin4u.domain.wallet.web.controller;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.exception.PrincipalNotReadable;
import com.line4thon.fin4u.domain.wallet.service.WalletService;
import com.line4thon.fin4u.domain.wallet.web.dto.CardReq;
import com.line4thon.fin4u.domain.wallet.web.dto.CheckingAccountReq;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;
import com.line4thon.fin4u.domain.wallet.web.dto.SavingAccountReq;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForSave;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForUpdate;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Validated
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
            throw new PrincipalNotReadable();
        }

        MainWalletRes response = walletService.getWalletMainPage(getMemberId(principal.getName()));

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PostMapping("/card")
    public ResponseEntity<SuccessResponse<?>> addCard(
            Principal principal,
            @RequestBody @Validated(ForSave.class) CardReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) {
            throw new PrincipalNotReadable();
        }
        MainWalletRes.Cards response = walletService.addCard(getMemberId(principal.getName()), request);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PostMapping("/check-account")
    public ResponseEntity<SuccessResponse<?>> addCheckingAccount(
            Principal principal,
            @RequestBody @Validated(ForSave.class) CheckingAccountReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank())
            throw new PrincipalNotReadable();
        MainWalletRes.CheckingAccounts response = walletService.addCheckingAccount(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PostMapping("/save-account")
    public ResponseEntity<SuccessResponse<?>> addSavingAccount(
            Principal principal,
            @RequestBody @Validated(ForSave.class) SavingAccountReq request
    ) {
        if (principal.getName().isEmpty() || principal.getName().isBlank()) throw new PrincipalNotReadable();
        MainWalletRes.SavingAccounts response = walletService.addSavingAccount(getMemberId(principal.getName()), request);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PutMapping("/card")
    public ResponseEntity<SuccessResponse<?>> modifyCardDetail(
            Principal principal,
            @RequestBody @Validated(ForUpdate.class) CardReq request
    ) {
        if (principal.getName().isEmpty() || principal.getName().isBlank()) {
            throw new PrincipalNotReadable();
        }
        MainWalletRes.Cards response = walletService.editCardDetail(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));

    }

    @PutMapping("/check-account")
    public ResponseEntity<SuccessResponse<?>> modifyCheckingAccount(
            Principal principal,
            @RequestBody @Validated(ForUpdate.class) CheckingAccountReq request
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank())
            throw new PrincipalNotReadable();
        MainWalletRes.CheckingAccounts response = walletService.editCheckAccountDetail(getMemberId(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @PutMapping("/save-account")
    public ResponseEntity<SuccessResponse<?>> modifySavingAccount(
            Principal principal,
            @RequestBody @Validated(ForUpdate.class) SavingAccountReq request
    ) {
        if (principal.getName().isEmpty() || principal.getName().isBlank()) throw new PrincipalNotReadable();
        MainWalletRes.SavingAccounts response = walletService.editSavingAccountDetail(getMemberId(principal.getName()), request);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }

    @DeleteMapping("/card/{cardId}")
    public ResponseEntity<SuccessResponse<?>> deleteCard(
            Principal principal,
            @PathVariable @NotNull Long cardId
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) throw new PrincipalNotReadable();
        walletService.deleteCard(getMemberId(principal.getName()), cardId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.empty());
    }

    @DeleteMapping("/check-account/{checkingAccountId}")
    public ResponseEntity<SuccessResponse<?>> deleteCheckingAccount(
            Principal principal,
            @PathVariable @NotNull Long checkingAccountId
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) throw new PrincipalNotReadable();
        walletService.deleteCheckAccount(getMemberId(principal.getName()), checkingAccountId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.empty());
    }

    @DeleteMapping("/save-account/g")
    public ResponseEntity<SuccessResponse<?>> deleteSavingAccount(
            Principal principal,
            @PathVariable @NotNull Long savingAccountId
    ) {
        if(principal.getName().isEmpty() || principal.getName().isBlank()) throw new PrincipalNotReadable();
        walletService.deleteSavingAccount(getMemberId(principal.getName()), savingAccountId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.empty());
    }
}
