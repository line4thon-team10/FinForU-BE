package com.line4thon.fin4u.domain.product.service.Product;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.exception.InvalidProductTypeException;
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
    private final CardBenefitRepository benefitRepository;

    //상품 조회
    @Override
    public ProductFilterRes getFilterProduct(ProductFilterReq filter) {

        List<ProductFilterRes.CardProductRes> cardRes = Collections.emptyList();
        List<ProductFilterRes.DepositProductRes> depositRes = Collections.emptyList();
        List<ProductFilterRes.SavingProductRes> savingRes = Collections.emptyList();

        Type productType = filter.type();

        // null값이면 true (사용자가 상품 선택을 하지 않았을때)
        boolean searchAll = (productType == null);

        // 1. 예금 상품 검색
        if (searchAll || productType == Type.DEPOSIT) {
            depositRes = searchDeposits(filter);
        }

        // 2. 카드 상품 검색
        if (searchAll || productType == Type.CARD) {
            cardRes = searchCards(filter);
        }

        // 3. 적금 상품 검색
        if (searchAll || productType == Type.SAVING) {
            savingRes = searchSavings(filter);
        }


        return new ProductFilterRes(
                cardRes,
                depositRes,
                savingRes
        );
    }

    // 상품 상세 조회
    @Override
    public ProductDetailRes getProductDetail(String type, Long id) {

        String productType = type.toLowerCase();

        ProductDetailRes.CardDetailRes cardDetail = null;
        ProductDetailRes.DepositDetailRes depositDetail = null;
        ProductDetailRes.SavingDetailRes savingDetail = null;

        switch(productType){
            case "card":
                Card card = cardRepo.findById(id)
                        .orElseThrow(NotFoundCardException::new);

                List<ProductDetailRes.CardBenefitDetail> cardBenefits = getCardBenefitsDetail(card);
                cardDetail = ProductDetailRes.CardDetailRes.fromCard(card, cardBenefits);
                break;
            case "deposit":
                Deposit deposit = depositRepo.findById(id)
                        .orElseThrow(NotFoundDepositException::new);
                depositDetail = ProductDetailRes.DepositDetailRes.fromDeposit(deposit);
                break;
            case "saving":
                InstallmentSaving saving = savingRepo.findById(id)
                        .orElseThrow(NotFoundSavingException::new);
                savingDetail = ProductDetailRes.SavingDetailRes.fromSaving(saving);
                break;
            default:
                throw new InvalidProductTypeException();
        }

        return new ProductDetailRes(
                cardDetail,
                depositDetail,
                savingDetail
        );
    }

    private List<ProductDetailRes.CardBenefitDetail> getCardBenefitsDetail(Card card){
        List<CardBenefit> benefits = benefitRepository.findByCardId(card.getId());
        return benefits.stream()
                .map(b ->{
                        String category = b.getBenefitCategory().name();
                        return new ProductDetailRes.CardBenefitDetail(category, b.getDescription());
                })
                .toList();
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
