package com.line4thon.fin4u.domain.recommend.web.dto;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.enums.BenefitCategory;
import com.line4thon.fin4u.global.util.BankNameTranslator;

import java.util.Collections;
import java.util.List;

public record RecommendRes(
        List<ResultItem> results
) {

    public interface ResultItem {
        String type();
    }

    public record CardItem(
            Long id,
            String type,
            String name,
            String bankName,
            String feeAndBenefit
    ) implements ResultItem {

        public static CardItem fromCard(Card card, String langCode, BankNameTranslator translator) {

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

            String feeAndBenefit = (promotion != null && !promotion.isBlank())
                    ? promotion + " and " + feeDescription
                    : feeDescription;

            String bankName = translator.translate(card.getBank().getBankName(), langCode);

            return new CardItem(
                    card.getId(),
                    "CARD",
                    card.getNameByLang(langCode),
                    bankName,
                    feeAndBenefit
            );
        }
    }

    public record DepositItem(
            Long id,
            String type,
            String name,
            String bankName,
            double maxInterestRate,
            Integer termMonths,
            boolean isFlexible
    ) implements ResultItem {

        public static DepositItem fromDeposit(Deposit deposit, String langCode, BankNameTranslator translator) {

            String bankName = translator.translate(deposit.getBank().getBankName(), langCode);

            return new DepositItem(
                    deposit.getId(),
                    "DEPOSIT",
                    deposit.getNameByLang(langCode),
                    bankName,
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm(),
                    deposit.getIsFlexible()
            );
        }
    }

    public record SavingItem(
            Long id,
            String type,
            String name,
            String bankName,
            double maxInterestRate,
            Integer termMonths,
            boolean isFlexible,
            int maxMonthly
    ) implements ResultItem {

        public static SavingItem fromSaving(InstallmentSaving saving, String langCode, BankNameTranslator translator) {

            String bankName = translator.translate(saving.getBank().getBankName(), langCode);

            return new SavingItem(
                    saving.getId(),
                    "SAVING",
                    saving.getNameByLang(langCode),
                    bankName,
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getIsFlexible(),
                    saving.getMaxMonthly()
            );
        }
    }
}
