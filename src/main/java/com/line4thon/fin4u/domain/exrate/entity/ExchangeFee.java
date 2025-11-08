package com.line4thon.fin4u.domain.exrate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeFee {

    SHINHAN("shinhan", 0.175, 2.0, 6.6),
    HANA("hana", 0.175, 3.5, 11.8),
    KOOKMIN("kookmin", 0.175, 3.0, 9.6),
    woori("woori", 0.175, 3.25, null);

    private final String bank;
    private final Double usdFee;
    private final Double cnyFee;
    private final Double vndFee;
}
