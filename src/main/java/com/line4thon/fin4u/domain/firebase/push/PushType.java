package com.line4thon.fin4u.domain.firebase.push;


import lombok.Getter;

@Getter
public enum PushType {

    // 🔹 적금 (Installment Savings)
    INSTALLMENT_DUE_TODAY(
            "오늘은 적금 납부일입니다.",
            "Today is your installment savings payment due date.",
            "installment_due_today"
    ),
    INSTALLMENT_MATURED(
            "적금 만기일입니다.",
            "Your installment savings have matured.",
            "installment_matured"
    ),
    INSTALLMENT_MATURING_SOON(
            "곧 적금 만기일입니다. 새로운 적금을 찾아보세요!",
            "Your installment savings mature soon. Consider a new plan!",
            "installment_maturing_soon"
    ),

    // 🔹 예금 (Deposit)
    DEPOSIT_DUE_TODAY(
            "오늘은 예금 납부일입니다.",
            "Today is your deposit payment due date.",
            "deposit_due_today"
    ),
    DEPOSIT_MATURED(
            "예금 만기일입니다.",
            "Your deposit has matured.",
            "deposit_matured"
    ),
    DEPOSIT_MATURING_SOON(
            "곧 예금 만기일입니다. 새로운 상품을 찾아보세요!",
            "Your deposit matures soon. Explore new options!",
            "deposit_maturing_soon"
    ),

    // 🔹 카드 (Card)
    CARD_DUE_TODAY(
            "오늘은 카드 납부일입니다.",
            "Today is your card payment due date.",
            "card_due_today"
    );

    private final String titleKo;   // 한글 제목
    private final String titleEn;   // 영어 제목
    private final String deeplink;  // 앱 라우팅용 키(ex: fin4u://notification/card_due_today)

    PushType(String titleKo, String titleEn, String deeplink) {
        this.titleKo = titleKo;
        this.titleEn = titleEn;
        this.deeplink = deeplink;
    }
}
