package com.line4thon.fin4u.domain.exrate.web.dto;

import java.time.LocalDate;
import java.util.List;

public record ExchangeRateRes(
        String currencyType,   // 외화 종류
        Double todayRate,                // 현재 가격
        Double rateCompareYesterday,  // 어제와 비교해서 n.n%
        // String duration,                // 기간
        List<Exchange> priceGraphData     // 환율 그래프를 위한 데이터(날짜와 가격)
) {
    public record Exchange(
            LocalDate date,
            Double price
    ) {

    }
}
