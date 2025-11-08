package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;

import java.util.List;

public interface SavingCustomRepository {
    List<InstallmentSaving> searchProducts(ProductFilterReq filter);
    List<InstallmentSaving> searchProducts(ProductFilterReq filter, List<Long> ids);
}
