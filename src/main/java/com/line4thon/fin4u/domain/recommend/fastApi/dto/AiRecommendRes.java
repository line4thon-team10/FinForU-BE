package com.line4thon.fin4u.domain.recommend.fastApi.dto;

import java.util.List;
//파이썬의 추천 응답
public record AiRecommendRes(
        List<AiRecItem> results
) {
    public record AiRecItem(
        String type,
        Long id
    ) {}
}
