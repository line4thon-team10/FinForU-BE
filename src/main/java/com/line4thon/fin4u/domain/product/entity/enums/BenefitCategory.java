package com.line4thon.fin4u.domain.product.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BenefitCategory {
    FLIGHT("비행", "Flight", "飞行", "Bay"),
    CAFE("카페", "Cafe", "咖啡", "Cà phê"),
    DISCOUNT("할인", "Discount", "折扣", "Giảm giá"),
    POINT("포인트", "Point", "积分", "Điểm"),
    PROMOTION("프로모션", "Promotion", "促销", "Khuyến mãi");

    private final String koName;
    private final String enName;
    private final String zhName;
    private final String viName;

    public String getNameByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "en" -> enName;
            case "zh" -> zhName;
            case "vi" -> viName;
            default -> koName;
        };
    }
}
