package com.line4thon.fin4u.domain.exrate.KoreanBank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    USD_CODE("USD", "0000001"),
    CNY_CODE("CNY", "0000053"),
    VND_CODE("VND", "0000035");

    public static String getCurrencyType(String currencyCode) {
        for(Country country : Country.values()) {
            if(country.getCurrencyCode().equals(currencyCode)) {
                return country.getCurrencyType();
            }
        }
        throw new IllegalArgumentException("잘못된 나라 코드를 입력했습니다.");
    }

    private final String currencyType;
    private final String currencyCode;
}
