package com.line4thon.fin4u.domain.product.web.dto;

import com.line4thon.fin4u.domain.product.entity.enums.Type;
import org.springframework.lang.Nullable;

public record ComparisonSaveReq (
        Type type,
        Long productId
){
}
