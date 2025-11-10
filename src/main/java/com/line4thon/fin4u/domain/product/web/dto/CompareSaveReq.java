package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.enums.Type;

public record CompareSaveReq(
        Type type,
        Long productId
){
}
