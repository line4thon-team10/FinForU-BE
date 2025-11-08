package com.line4thon.fin4u.domain.product.service;

import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;

public interface ProductService {
    ProductFilterRes getFilterProduct(ProductFilterReq filter);
}
