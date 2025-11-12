package com.line4thon.fin4u.domain.product.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.BenefitCategory;
import com.line4thon.fin4u.global.util.BankNameTranslator;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ProductFilterRes (
    List<CardProductRes> cards,
    List<DepositProductRes> deposits,
    List<SavingProductRes> savings
){
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record CardProductRes(
            Long id,
            String name,
            String bankName,
            String feeAndBenefit //프로모션 + 연회비
    ) {
        public static CardProductRes fromCard(Card card, String langCode, BankNameTranslator translator) {

            List<CardBenefit> benefits = card.getCardBenefit() == null
                    ? Collections.emptyList()
                    : card.getCardBenefit();

            String promotion = benefits.stream()
                    .filter(b -> b.getBenefitCategory() == BenefitCategory.PROMOTION)
                    .map(b -> b.getDescriptionByLang(langCode))
                    .findFirst()
                    .orElse(null);

            int annualFee = (card.getInternationalAnnualFee() > 0)
                    ? card.getInternationalAnnualFee()
                    : card.getDomesticAnnualFee();

            String feeDescription;
            if (annualFee == 0) {
                feeDescription = switch (langCode.toLowerCase()) {
                    case "zh" -> "无年费";
                    case "vi" -> "miễn phí thường niên";
                    default -> "No annual fee";
                };
            } else {
                feeDescription = switch (langCode.toLowerCase()) {
                    case "zh" -> annualFee + "韩元年费";
                    case "vi" -> "phí thường niên " + annualFee + " won";
                    default -> annualFee + " won fee";
                };
            }
            String finalOutput = (promotion != null && !promotion.isBlank())
                    ? promotion + " and " + feeDescription
                    : feeDescription;


            String translatedBank = translator.translate(card.getBank().getBankName(), langCode);

            return new CardProductRes(
                    card.getId(),
                    card.getNameByLang(langCode),
                    translatedBank,
                    finalOutput
            );
        }

    }
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record DepositProductRes(
            Long id,
            String name,
            String bankName,
            double maxInterestRate,
            Integer termMonths,
            boolean isFlexible
    ){
        public static DepositProductRes fromDeposit(Deposit deposit, String langCode, BankNameTranslator translator){
            String translatedBank = translator.translate(deposit.getBank().getBankName(), langCode);

            return new DepositProductRes(
                    deposit.getId(),
                    deposit.getNameByLang(langCode),
                    translatedBank,
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm(),
                    deposit.getIsFlexible()
            );
        }
    }
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record SavingProductRes(
            Long id,
            String name,
            String bankName,
            double maxInterestRate,
            Integer termMonths,
            boolean isFlexible,
            int maxMonthly //월 최대 납입 가능 금액
    ){
        public static SavingProductRes fromSaving(InstallmentSaving saving, String langCode, BankNameTranslator translator) {
            String translatedBank = translator.translate(saving.getBank().getBankName(), langCode);

            return new SavingProductRes(
                    saving.getId(),
                    saving.getNameByLang(langCode),
                    translatedBank,
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getIsFlexible(),
                    saving.getMaxMonthly()
            );
        }
    }
}
