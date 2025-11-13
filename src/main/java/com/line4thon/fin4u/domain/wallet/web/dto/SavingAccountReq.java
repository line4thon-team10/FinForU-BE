package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Account;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForSave;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SavingAccountReq {
    @NotNull(groups = ForUpdate.class, message = "ID 필드는 비어있을 수 없습니다.")
    private Long savingAccountId;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "은행은 비어있을 수 없습니다.")
    private Bank bank;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "상품 종류는 비어있을 수 없습니다.")
    private Account productType;

    @NotBlank(groups = {ForSave.class, ForUpdate.class}, message = "상품 이름은 비어있을 수 없습니다.")
    private String productName;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "AccountPeriod는 비어있을 수 없습니다.")
    private LocalDate start;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "AccountPeriod는 비어있을 수 없습니다.")
    private LocalDate end;

    private Integer monthlyPayment;

    private Integer upcomingDate;
}
