package com.line4thon.fin4u.domain.recommend.fastApi.dto;

import java.util.List;
//상품 선호도를 이용한 추천 요청
public record AiRecommendReq(
        List<String> types,
        List<String> periods,
        String savingPurpose,
        String cardPurpose,
        String preferBank,
        String income
) { }