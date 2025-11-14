package com.line4thon.fin4u.domain.recommend.fastApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

//상품 데이터들을 파이썬에 보낼 형식
@JsonInclude(JsonInclude.Include.ALWAYS)
@Builder
public class UpsertRes {

    @Builder
    public record UpsertItem(
            // 필수적
            String id,
            String type,
            String text_for_embed,

            //사용자 선택
            String bank,

            //예금, 적금
            String term,  //예적금 기간
            Double base_rate, //기본 금리
            Double max_rate, //최대금리

            //카드
            Integer annual_fee_domestic, //국내연회비
            Integer annual_fee_international, //국외연회비
            String card_type,
            List<String> benefit_tags
    ) {
        public UpsertItem {
            //null값이 가지 않도록 기본 설정
            if (id == null) id = "";
            if(bank == null) bank="";

            if(term == null) term = "";
            if(base_rate == null) base_rate = 0.0;
            if(max_rate == null) max_rate = 0.0;

            if(annual_fee_domestic == null) annual_fee_domestic =0;
            if(annual_fee_international == null) annual_fee_international = 0;
            if(card_type == null) card_type = "";
            if (benefit_tags == null) benefit_tags = List.of();
        }
    }

    @Builder
    public record UpsertBody(
            List<UpsertItem> items
    ) {}

}
