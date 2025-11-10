package com.line4thon.fin4u.domain.wallet.web.controller;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.wallet.service.WalletService;
import com.line4thon.fin4u.domain.wallet.web.dto.MainWalletRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
