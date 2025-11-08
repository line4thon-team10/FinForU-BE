package com.line4thon.fin4u.domain.product.service;

import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CardRepository cardRepo;
    private final DepositRepository depositRepo;
    private final InstallmentSavingRepository savingRepo;


    //상품 조회
    @Override
    public ProductFilterRes getFilterProduct(ProductFilterReq filter) {

        String productType = filter.type();

        List<ProductFilterRes.CardProductRes> cardResponses = Collections.emptyList();
        List<ProductFilterRes.DepositProductRes> depositResponses = Collections.emptyList();
        List<ProductFilterRes.SavingProductRes> savingResponses = Collections.emptyList();

        // null값이면 true (사용자가 상품 선택을 하지 않았을때)
        boolean searchAll = filter.type() == null;

        // 1. 예금 상품 검색
        if (searchAll || "deposit".equalsIgnoreCase(productType)) {
            depositResponses = searchDeposits(filter);
        }

        // 2. 카드 상품 검색
        if (searchAll || "card".equalsIgnoreCase(productType)) {
            cardResponses = searchCards(filter);
        }

        // 3. 적금 상품 검색
        if (searchAll || "saving".equalsIgnoreCase(productType)) {
            savingResponses = searchSavings(filter);
        }


        return new ProductFilterRes(
                cardResponses,
                depositResponses,
                savingResponses
        );
    }

    // 카드 상품 필터링 검색
    private List<ProductFilterRes.CardProductRes> searchCards(ProductFilterReq filter) {
        return cardRepo.searchProducts(filter).stream()
                .map(ProductFilterRes.CardProductRes::fromCard)
                .toList();
    }

    // 예금 상품 필터링 검색
    private List<ProductFilterRes.DepositProductRes> searchDeposits(ProductFilterReq filter) {
        return depositRepo.searchProducts(filter).stream()
                .map(ProductFilterRes.DepositProductRes::fromDeposit)
                .toList();
    }

    // 적금 상품 필터링 검색
    private List<ProductFilterRes.SavingProductRes> searchSavings(ProductFilterReq filter){
        return savingRepo.searchProducts(filter).stream()
                .map(ProductFilterRes.SavingProductRes::fromSaving)
                .toList();
    }
}
