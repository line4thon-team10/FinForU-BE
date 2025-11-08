package com.line4thon.fin4u.domain.product.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;

import java.util.List;

public record ProductDetailRes (
        CardDetailRes cardDetail,
        DepositDetailRes depositDetail,
        SavingDetailRes savingDetail
){
    // 카드 혜택
    public record CardBenefitDetail(
            String category,
            String description
    ) {}
    // 자격 조건
    public record Eligibility(
            Integer age,
            boolean validId,
            boolean resident
    ){}


    /// 카드 상세
    public record CardDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            int DomesticAnnualFee,
            int internationalAnnualFee,
            Eligibility eligibility,
            List<CardBenefitDetail> benefits

    ){
        public static CardDetailRes fromCard(Card card, List<CardBenefitDetail> benefits) {

            Eligibility eligibilityDto = new Eligibility(
                    card.getMinAge(),
                    card.getIdRequired(),
                    card.getIsResident()
            );
            return new CardDetailRes(
                    card.getId(),
                    card.getName(),
                    card.getBank().getBankName(),
                    card.getDescription(),
                    card.getDomesticAnnualFee(),
                    card.getInternationalAnnualFee(),
                    eligibilityDto,
                    benefits
            );
        }
    }

    /// 예금
    public record DepositDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer minDepositAmount,
            Eligibility eligibility
    ){
        public static DepositDetailRes fromDeposit(Deposit deposit) {

            Eligibility eligibilityDto = new Eligibility(
                    deposit.getMinAge(),
                    deposit.getIdRequired(),
                    deposit.getIsResident()
            );
            return new DepositDetailRes(
                    deposit.getId(),
                    deposit.getName(),
                    deposit.getBank().getBankName(),
                    deposit.getDescription(),
                    deposit.getBaseInterestRate(),
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm(),
                    deposit.getIsFlexible(),
                    deposit.getMinDepositAmount(),
                    eligibilityDto
            );
        }
    }

    /// 적금
    public record SavingDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer maxMonthly,
            Eligibility eligibility
    ){
        public static SavingDetailRes fromSaving(InstallmentSaving saving) {

            Eligibility eligibilityDto = new Eligibility(
                    saving.getMinAge(),
                    saving.getIdRequired(),
                    saving.getIsResident()
            );
            return new SavingDetailRes(
                    saving.getId(),
                    saving.getName(),
                    saving.getBank().getBankName(),
                    saving.getDescription(),
                    saving.getBaseInterestRate(),
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getIsFlexible(),
                    saving.getMaxMonthly(),
                    eligibilityDto
            );
        }
    }
}