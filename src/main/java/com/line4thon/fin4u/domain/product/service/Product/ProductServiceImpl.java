package com.line4thon.fin4u.domain.product.service.Product;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.exception.NotFoundCardException;
import com.line4thon.fin4u.domain.product.exception.NotFoundDepositException;
import com.line4thon.fin4u.domain.product.exception.NotFoundSavingException;
import com.line4thon.fin4u.domain.product.repository.CardBenefitRepository;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.ProductDetailRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.util.BankNameTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final BankNameTranslator translator;

    private final CardRepository cardRepo;
    private final DepositRepository depositRepo;
    private final InstallmentSavingRepository savingRepo;
    private final CardBenefitRepository benefitRepository;

    //상품 조회
    @Override
    public ProductFilterRes getFilterProduct(ProductFilterReq filter, String langCode) {

        List<ProductFilterRes.CardProductRes> cardRes = new ArrayList<>();
        List<ProductFilterRes.DepositProductRes> depositRes = new ArrayList<>();
        List<ProductFilterRes.SavingProductRes> savingRes = new ArrayList<>();

        Type productType = filter.type();

        // null값이면 true (사용자가 상품 선택을 하지 않았을때)
        boolean searchAll = (productType == null);

        // 금리, 기간 필터링시 카드 x
        boolean hasRateOrTerm = filter.minRate() != null
                || filter.maxRate() != null
                || filter.termMonths() != null;

        // 1. 예금 상품 검색
        if (searchAll || productType == Type.DEPOSIT) {
            depositRes = searchDeposits(filter, langCode);
        }

        // 2. 카드 상품 검색
        if ((searchAll || productType == Type.CARD) && !hasRateOrTerm) {
            cardRes = searchCards(filter, langCode);
        }

        // 3. 적금 상품 검색
        if (searchAll || productType == Type.SAVING) {
            savingRes = searchSavings(filter, langCode);
        }


        return new ProductFilterRes(
                cardRes,
                depositRes,
                savingRes
        );
    }

    // 상품 상세 조회
    public ProductDetailRes getProductDetail(Type type, Long id, String langCode) {

        List<ProductDetailRes.CardDetailRes> cardDetail = null;
        List<ProductDetailRes.DepositDetailRes> depositDetail = null;
        List<ProductDetailRes.SavingDetailRes> savingDetail = null;

        switch (type) {
            case CARD -> {
                Card card = cardRepo.findById(id)
                        .orElseThrow(NotFoundCardException::new);
                ProductDetailRes.CardDetailRes detail = ProductDetailRes.CardDetailRes.fromCard(card, langCode, translator);
                cardDetail = List.of(detail);
            }
            case DEPOSIT -> {
                Deposit deposit = depositRepo.findById(id)
                        .orElseThrow(NotFoundDepositException::new);
                ProductDetailRes.DepositDetailRes detail = ProductDetailRes.DepositDetailRes.fromDeposit(deposit, langCode, translator);
                depositDetail = List.of(detail);
            }
            case SAVING -> {
                InstallmentSaving saving = savingRepo.findById(id)
                        .orElseThrow(NotFoundSavingException::new);
                ProductDetailRes.SavingDetailRes detail = ProductDetailRes.SavingDetailRes.fromSaving(saving, langCode, translator);
                savingDetail = List.of(detail);
            }
        }

        return new ProductDetailRes(
                cardDetail,
                depositDetail,
                savingDetail
        );
    }


    // 카드 상품 필터링 검색
    private List<ProductFilterRes.CardProductRes> searchCards(ProductFilterReq filter, String langCode) {
        return cardRepo.searchProducts(filter).stream()
                .map(card -> ProductFilterRes.CardProductRes.fromCard(card, langCode, translator))
                .toList();
    }

    // 예금 상품 필터링 검색
    private List<ProductFilterRes.DepositProductRes> searchDeposits(ProductFilterReq filter, String langCode) {
        return depositRepo.searchProducts(filter).stream()
                .map(deposit -> ProductFilterRes.DepositProductRes.fromDeposit(deposit, langCode, translator))
                .toList();
    }

    // 적금 상품 필터링 검색
    private List<ProductFilterRes.SavingProductRes> searchSavings(ProductFilterReq filter, String langCode) {
        return savingRepo.searchProducts(filter).stream()
                .map(saving -> ProductFilterRes.SavingProductRes.fromSaving(saving, langCode, translator))
                .toList();
    }
}
