package com.line4thon.fin4u.domain.product.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.global.util.BankNameTranslator;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
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


    /// 카드 상세
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record CardDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            int domesticAnnualFee,
            int internationalAnnualFee,
            List<CardBenefitDetail> benefits

    ){
        public static CardDetailRes fromCard(Card card, List<CardBenefitDetail> benefits, String langCode, BankNameTranslator translator) {

            String translatedBank = translator.translate(card.getBank().getBankName(), langCode);

            return new CardDetailRes(
                    card.getId(),
                    card.getNameByLang(langCode),
                    translatedBank,
                    card.getDescriptionByLang(langCode),
                    card.getDomesticAnnualFee(),
                    card.getInternationalAnnualFee(),
                    benefits
            );
        }
    }

    /// 예금
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record DepositDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer minDepositAmount
    ){
        public static DepositDetailRes fromDeposit(Deposit deposit, String langCode, BankNameTranslator translator) {

            String translatedBank = translator.translate(deposit.getBank().getBankName(), langCode);

            return new DepositDetailRes(
                    deposit.getId(),
                    deposit.getNameByLang(langCode),
                    translatedBank,
                    deposit.getDescriptionByLang(langCode),
                    deposit.getBaseInterestRate(),
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm(),
                    deposit.getIsFlexible(),
                    deposit.getMinDepositAmount()
            );
        }
    }

    /// 적금
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record SavingDetailRes(
            Long id,
            String name,
            String bank,
            String description,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer maxMonthly
    ){
        public static SavingDetailRes fromSaving(InstallmentSaving saving, String langCode, BankNameTranslator translator) {

            String translatedBank = translator.translate(saving.getBank().getBankName(), langCode);

            return new SavingDetailRes(
                    saving.getId(),
                    saving.getNameByLang(langCode),
                    translatedBank,
                    saving.getDescriptionByLang(langCode),
                    saving.getBaseInterestRate(),
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getIsFlexible(),
                    saving.getMaxMonthly()
            );
        }
    }
}