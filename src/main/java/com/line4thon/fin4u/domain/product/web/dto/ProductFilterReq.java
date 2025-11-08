package com.line4thon.fin4u.domain.product.web.dto;

public record ProductFilterReq(
        String bank,
        String type,
        Double minRate,
        Double maxRate,
        Integer termMonths
) {
}
