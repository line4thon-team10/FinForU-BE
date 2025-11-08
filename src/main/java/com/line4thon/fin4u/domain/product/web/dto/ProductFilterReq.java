package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.enums.Type;

public record ProductFilterReq(
        String bank,
        Type type,
        Double minRate,
        Double maxRate,
        Integer termMonths
) {
}
