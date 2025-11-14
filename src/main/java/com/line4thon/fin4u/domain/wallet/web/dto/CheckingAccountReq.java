package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForSave;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckingAccountReq {
    @NotNull(groups = ForUpdate.class, message = "ID 필드는 비어있을 수 없습니다.")
    private Long checkingAccountId;

    @NotNull(groups = {ForSave.class}, message = "은행은 비어있을 수 없습니다.")
    private Bank bank;
}
