package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Account;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SavingAccountReq {
    private Long savingAccountId;

    @NotNull(message = "은행은 비어있을 수 없습니다.")
    private Bank bank;

    @NotNull(message = "상품 종류는 비어있을 수 없습니다.")
    private Account productType;

    @NotBlank(message = "상품 이름은 비어있을 수 없습니다.")
    private String productName;

    @NotNull(message = "AccountPeriod는 비어있을 수 없습니다.")
    private LocalDate start;

    @NotNull(message = "AccountPeriod는 비어있을 수 없습니다.")
    private LocalDate end;

    private Integer monthlyPayment;

    private Integer upcomingDate;
}
