package com.line4thon.fin4u.domain.recommend.fastApi;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;

import java.util.List;
import java.util.Locale;

// 벡터db에 저장할 상품 정보들
public class EmbedTextBuilder {

    //예적금 기간
    public static String term(Integer months){
        if(months == null) return "";
        if(months <= 12) return "short";
        if(months<= 36) return "mid";
        return "long";
    }

    //빈칸 줄이기, 최대길이 120자
    private static String shortDesc(String s) {
        if (s == null) return "";
        String t = s.replaceAll("\\s+", " ").trim();
        if (t.length() > 120) t = t.substring(0, 120);
        return t;
    }


    /**
     * 카드 정보 임베딩 텍스트
     * 은행 + 카드명 + 타입 + 혜택 + 한줄 설명
     */
    public static String cardText(Card card){
        String bank = card.getBank().getBankName();
        String kind = card.getCardType() == CardType.CHECK ? "check card" : "credit card";

        String benefitDesc = "";
        if (card.getCardBenefit() != null && !card.getCardBenefit().isEmpty()) {
            List<String> benefitTexts = card.getCardBenefit().stream()
                    .map(CardBenefit::getDescriptionEn)
                    .map(EmbedTextBuilder::shortDesc)
                    .toList();
            benefitDesc = "Benefits: " + String.join(", ", benefitTexts) + ".";
        }

        String description = shortDesc(card.getDescriptionEn());
        String descPart = description.isBlank()
                ? ""
                : "Description: " + description;

        return String.format(
                "%s %s: %s. %s. %s",
                bank, card.getNameEn(), kind, benefitDesc, descPart
        ).trim();
    }

    /**
     * 예금 정보 임베딩 텍스트
     * 은행 + 예금명 + 기간유연성 + 한줄 설명
     */
    public static String depositText(Deposit deposit) {
        String bank = deposit.getBank().getBankName();
        String flexible = Boolean.TRUE.equals(deposit.getIsFlexible()) ? "yes" : "no";

        String description = shortDesc(deposit.getDescriptionEn());
        String descPart = description.isBlank()
                ? ""
                : "Description: " + description;

        return String.format(
                "%s %s time deposit: flexible %s .%s",
                bank, deposit.getNameEn(), flexible, descPart
        ).trim();
    }

    /**
     * 적금 정보 임베딩 텍스트
     * 은행 + 적금 + 기간유연성 + 한줄 설명
     */
    public static String savingText(InstallmentSaving saving) {
        String bank = saving.getBank().getBankName();
        String flexible = Boolean.TRUE.equals(saving.getIsFlexible()) ? "yes" : "no";

        String description = shortDesc(saving.getDescriptionEn());
        String descPart = description.isBlank()
                ? ""
                : "Description: " + description;

        return String.format(
                "%s %s installment savings: flexible %s.%s",
                bank, saving.getNameEn(), flexible, descPart
        ).trim();
    }
}
