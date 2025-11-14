package com.line4thon.fin4u.domain.recommend.service;

import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;

import java.security.Principal;

public interface RecommendService {
    ProductFilterRes recommend(Principal principal, String langCode);
}
