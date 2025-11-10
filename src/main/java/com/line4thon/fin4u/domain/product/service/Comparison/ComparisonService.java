package com.line4thon.fin4u.domain.product.service.Comparison;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;

public interface ComparisonService {
    void saveProduct(String email, String token, Type type, Long productId);

    ProductFilterRes getComparisonFilter(String email, String guestToken, ProductFilterReq req);
}
