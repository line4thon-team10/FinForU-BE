package com.line4thon.fin4u.domain.product.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BenefitCategory {
    PUBLIC_TRANSIT("Public Transit Discount", "公共交通折扣", "Giảm giá giao thông công cộng"),
    SHOPPING("Shopping Discount", "购物折扣", "Giảm giá mua sắm"),
    TELECOM("Telecom Cashback", "通信费返现", "Hoàn tiền viễn thông"),
    CONVENIENCE("Convenience Store Cashback", "便利店返现", "Hoàn tiền cửa hàng tiện lợi"),
    RESTAURANT("Restaurant Cashback", "餐厅返现", "Hoàn tiền nhà hàng"),
    HOSPITAL_PHARMACY("Hospital & Pharmacy Cashback", "医院/药店返现", "Hoàn tiền bệnh viện & nhà thuốc"),
    SUBSCRIPTION("Subscription Cashback", "订阅服务返现", "Hoàn tiền dịch vụ đăng ký"),
    OVERSEAS_SPEND("Overseas Spending Benefits", "海外消费优惠", "Ưu đãi chi tiêu nước ngoài"),
    FX("Foreign Exchange Benefits", "外汇优惠", "Ưu đãi tỷ giá"),
    MOVIE("Movie Discount", "电影折扣", "Giảm giá phim"),
    INSURANCE("Insurance Cashback", "保险返现", "Hoàn tiền bảo hiểm"),
    COFFEE("Coffee Cashback", "咖啡返现", "Hoàn tiền cà phê"),
    DELIVERY_APP("Delivery App Cashback", "外卖应用返现", "Hoàn tiền ứng dụng giao hàng"),
    DAILY_SPEND("Daily Spending Rewards", "日常消费奖励", "Ưu đãi chi tiêu hàng ngày"),
    FOREIGNER_PERKS("For Foreign Residents", "外籍客户专享", "Dành cho người nước ngoài"),
    PROMOTION("Promotional Offer", "促销优惠", "Ưu đãi khuyến mãi");

    private final String enName;
    private final String zhName;
    private final String viName;

    public String getNameByLang(String langCode) {
        if (langCode == null) return enName;
        return switch (langCode.toLowerCase()) {
            case "zh" -> zhName;
            case "vi" -> viName;
            default -> enName;
        };
    }
}
