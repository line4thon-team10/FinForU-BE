package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.BenefitCategory;

import java.util.Collections;
import java.util.List;

public record ProductFilterRes (
    List<CardProductRes> cards,
    List<DepositProductRes> deposits,
    List<SavingProductRes> savings
){
    public record CardProductRes(
            Long id,
            String name,
            String bankName,
            String feeAndBenefit //프로모션 + 연회비
    ) {
        public static CardProductRes fromCard(Card card) {

            List<CardBenefit> benefits = card.getCardBenefit() == null
                    ? Collections.emptyList()
                    : card.getCardBenefit();

            String promotion = benefits.stream()
                    .filter(b -> b.getBenefitCategory() == BenefitCategory.PROMOTION)
                    .map(CardBenefit::getDescription)
                    .findFirst()
                    .orElse(null);

            String fee = (card.getDomesticAnnualFee() == 0) ? "no annual fee" : card.getDomesticAnnualFee()+"won fee.";

            String finalOutput = (promotion != null && !promotion.isBlank()) ? promotion + " and " + fee : fee;

            return new CardProductRes(
                    card.getId(),
                    card.getName(),
                    card.getBank().getBankName(),
                    finalOutput

            );
        }
    }

    public record DepositProductRes(
            Long id,
            String name,
            String bankName,
            double maxInterestRate,
            int termMonths
    ){
        public static DepositProductRes fromDeposit(Deposit deposit){
            return new DepositProductRes(
                    deposit.getId(),
                    deposit.getName(),
                    deposit.getBank().getBankName(),
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm()
            );
        }
    }

    public record SavingProductRes(
            Long id,
            String name,
            String bankName,
            double maxInterestRate,
            int termMonths,
            int maxMonthly //월 최대 납입 가능 금액
    ){
        public static SavingProductRes fromSaving(InstallmentSaving saving) {
            return new SavingProductRes(
                    saving.getId(),
                    saving.getName(),
                    saving.getBank().getBankName(),
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getMaxMonthly()
            );
        }
    }
}
