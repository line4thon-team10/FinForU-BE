package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import java.util.List;

//동적 쿼리 기능
public interface CardCustomRepository {

    List<Card> searchProducts(ProductFilterReq filter);
    List<Card> searchProducts(ProductFilterReq filter, List<Long> ids);
}
