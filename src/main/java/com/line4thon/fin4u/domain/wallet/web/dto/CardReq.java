package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.CardType;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForSave;
import com.line4thon.fin4u.domain.wallet.web.dto.groups.ForUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardReq {
    @NotNull(groups = ForUpdate.class, message = "ID 필드는 비어있을 수 없습니다.")
    private Long cardId;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "은행 종류는 비어있을 수 없습니다.")
    private Bank bank;

    @NotNull(groups = {ForSave.class, ForUpdate.class}, message = "카드 종류는 비어있을 수 없습니다.")
    private CardType cardType;

    @NotBlank(groups = {ForSave.class, ForUpdate.class}, message = "상품 별명은 비어있을 수 없습니다.")
    private String cardName;

    private LocalDate upcomingDate;
}
