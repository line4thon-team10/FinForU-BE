package com.line4thon.fin4u.domain.wallet.web.dto;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardReq {
    private Long cardId;

    @NotNull(message = "은행 종류는 비어있을 수 없습니다.")
    private Bank bank;

    @NotNull(message = "카드 종류는 비어있을 수 없습니다.")
    private CardType cardType;

    @NotBlank(message = "상품 별명은 비어있을 수 없습니다.")
    private String cardName;

    private LocalDate upcomingDate;
}
