package com.line4thon.fin4u.global.crawl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankCode {
    KOOKMIN("kookmin", "0010927"),
    SHINHAN("shinhan", "0011625"),
    WOORI("woori", "0010001"),
    HANA("hana", "0013909");

    private final String bank;
    private final String code;
}
