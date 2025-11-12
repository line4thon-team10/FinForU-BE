package com.line4thon.fin4u.domain.product.service.Product;

import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.web.dto.ProductDetailRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;

public interface ProductService {

    ProductDetailRes getProductDetail(Type type, Long id, String langCode);

    ProductFilterRes getFilterProduct(ProductFilterReq filter, String langCode);
}
