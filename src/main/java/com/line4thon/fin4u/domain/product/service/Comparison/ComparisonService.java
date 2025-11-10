package com.line4thon.fin4u.domain.product.service.Comparison;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.web.dto.CompareRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;

import java.security.Principal;
import java.util.List;

public interface ComparisonService {
    void saveProduct(Principal principal, String token, Type type, Long productId);

    ProductFilterRes getComparisonFilter(Principal principal, String guestToken, ProductFilterReq req);

    CompareRes compare(List<Long> productIds, Type type);
}
