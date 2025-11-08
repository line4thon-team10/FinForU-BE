package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;

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
            int annualFee
    ) {
        public static CardProductRes fromCard(Card card) {
            return new CardProductRes(
                    card.getId(),
                    card.getName(),
                    card.getBank().getBankName(),
                    card.getAnnualFee()
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
