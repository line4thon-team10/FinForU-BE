package com.line4thon.fin4u.domain.exrate.web.dto;

import java.util.List;

public record ExchangeData(
        Statics StatisticSearch
) {
    public record Statics(
            Integer list_total_count,
            List<KoreanBankRes> row
    ) {
        public record KoreanBankRes(
                String STAT_CODE,
                String STAT_NAME,
                String ITEM_CODE1,
                String ITEM_NAME1,
                String ITEM_CODE2,
                String ITEM_NAME2,
                String ITEM_CODE3,
                String ITEM_NAME3,
                String ITEM_CODE4,
                String ITEM_NAME4,
                String UNIT_NAME,
                String WGT,
                String TIME,
                String DATA_VALUE
        ) {}
    }
}
