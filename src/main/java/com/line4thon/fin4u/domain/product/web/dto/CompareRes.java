package com.line4thon.fin4u.domain.product.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.global.util.BankNameTranslator;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CompareRes(
        List<CardCompareRes> cards,
        List<DepositCompareRes> deposits,
        List<SavingCompareRes> savings,
        Highlights highlights
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Highlights(
            Long bestBaseRateId,            // 예금/적금: 최대 기본금리
            Long bestMaxRateId,             // 예금/적금: 최대 최고금리
            Long lowestMinDepositId,        // 예금: 최소 예치금
            Long lowestMaxMonthlyId,        // 적금: 최소 월 납입 한도
            Long lowestDomesticId,          // 카드: 최소 국내 연회비
            Long lowestInternationalId      // 카드: 최소 해외 연회비
    ) {}

    public static Highlights cardHighlight(Long lowestDomesticId, Long lowestInternationalId) {
        return new Highlights(
                null,
                null,
                null,
                null,
                lowestDomesticId,
                lowestInternationalId
        );
    }

    public static Highlights depositHighlight(Long bestBaseRateId, Long bestMaxRateId, Long lowestMinDepositId) {
        return new Highlights(
                bestBaseRateId,
                bestMaxRateId,
                lowestMinDepositId,
                null,
                null,
                null
        );
    }

    public static Highlights savingHighlight(Long bestBaseRateId, Long bestMaxRateId, Long lowestLimitId) {
        return new Highlights(
                bestBaseRateId,
                bestMaxRateId,
                null,
                lowestLimitId,
                null,
                null
        );
    }


    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record CardCompareRes(
            Long id,
            String name,
            String bank,
            int domesticAnnualFee,
            int internationalAnnualFee,
            List<String> benefit,
            String website
    ) {
        public static CardCompareRes fromCard(Card card, String langCode, BankNameTranslator translator) {
            String translatedBank = translator.translate(card.getBank().getBankName(), langCode);

            List<String> benefits = card.getCardBenefit().stream()
                    .map(benefit -> benefit.getDescriptionByLang(langCode))
                    .toList();

            return new CardCompareRes(
                    card.getId(),
                    card.getNameByLang(langCode),
                    translatedBank,
                    card.getDomesticAnnualFee(),
                    card.getInternationalAnnualFee(),
                    benefits,
                    card.getOfficialWebsite()
            );
        }
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record DepositCompareRes(
            Long id,
            String name,
            String bank,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer minDepositAmount,
            String website
    ) {
        public static DepositCompareRes fromDeposit(Deposit deposit, String langCode, BankNameTranslator translator) {
            String translatedBank = translator.translate(deposit.getBank().getBankName(), langCode);

            return new DepositCompareRes(
                    deposit.getId(),
                    deposit.getNameByLang(langCode),
                    translatedBank,
                    deposit.getBaseInterestRate(),
                    deposit.getMaxInterestRate(),
                    deposit.getDepositTerm(),
                    deposit.getIsFlexible(),
                    deposit.getMinDepositAmount(),
                    deposit.getOfficialWebsite()
            );
        }
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record SavingCompareRes(
            Long id,
            String name,
            String bank,
            Double baseRate,
            Double maxRate,
            Integer termMonths,
            Boolean isFlexible,
            Integer maxMonthly,
            String website
    ) {
        public static SavingCompareRes fromSaving(InstallmentSaving saving, String langCode, BankNameTranslator translator) {
            String translatedBank = translator.translate(saving.getBank().getBankName(), langCode);

            return new SavingCompareRes(
                    saving.getId(),
                    saving.getNameByLang(langCode),
                    translatedBank,
                    saving.getBaseInterestRate(),
                    saving.getMaxInterestRate(),
                    saving.getSavingTerm(),
                    saving.getIsFlexible(),
                    saving.getMaxMonthly(),
                    saving.getOfficialWebsite()
            );
        }
    }
}
