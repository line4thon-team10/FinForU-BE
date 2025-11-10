package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckingAccountReq {
    @NotNull(message = "은행은 비어있을 수 없습니다.")
    private Bank bank;
}
