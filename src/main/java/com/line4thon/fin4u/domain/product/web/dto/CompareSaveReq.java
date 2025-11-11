package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.enums.Type;

import javax.validation.constraints.NotNull;

public record CompareSaveReq(
        @NotNull(message = "상품 유형(type)은 필수 값입니다.")
        Type type,
        @NotNull(message = "productId는 필수 값입니다.")
        Long productId
){
}
