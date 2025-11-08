package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SavingCustomRepository {
    List<InstallmentSaving> searchProducts(ProductFilterReq filter);
    List<InstallmentSaving> searchProducts(ProductFilterReq filter, List<Long> ids);
}
