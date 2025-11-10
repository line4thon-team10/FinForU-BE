package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import org.springframework.stereotype.Repository;

import java.util.List;

//동적 쿼리 기능
@Repository
public interface DepositCustomRepository {
    List<Deposit> searchProducts(ProductFilterReq filter);
    List<Deposit> searchProducts(ProductFilterReq filter, List<Long> ids);
}
