package com.line4thon.fin4u.domain.product.service.Comparison;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.product.entity.Comparison;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.exception.NotFoundCardException;
import com.line4thon.fin4u.domain.product.exception.NotFoundDepositException;
import com.line4thon.fin4u.domain.product.exception.NotFoundGuestTokenException;
import com.line4thon.fin4u.domain.product.exception.NotFoundSavingException;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.ComparisonRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService{

    private final MemberRepository memberRepo;
    private final CardRepository cardRepo;
    private final DepositRepository depositRepo;
    private final InstallmentSavingRepository savingRepo;
    private final ComparisonRepository comparisonRepo;

    //바구니 저장
    @Override
    @Transactional
    public void saveProduct(Member member, String guestToken, Type type, Long productId) {

        validateProductExists(type, productId);

        if(member != null) {
            memberRepo.findById(member.getMemberId())
                    .orElseThrow(MemberNotFoundException::new);
            comparisonRepo.save(Comparison.of(member, type, productId));
            return;
        }
        //에러처리하기
        if(guestToken == null || guestToken.isBlank())
            throw new NotFoundGuestTokenException();

        comparisonRepo.save(Comparison.ofGuest(guestToken, type, productId));
    }

    // 바구니 조회&필터링
    @Override
    public ProductFilterRes getComparisonFilter(Member member, String guestToken, ProductFilterReq filter) {
        // 1. 바구니 조회
        List<Comparison> products = (member != null)
                ? comparisonRepo.findByMember(member)
                : comparisonRepo.findByGuestToken(guestToken);

        // 2. 타입별 분류
        List<Long> cardIds = new ArrayList<>();
        List<Long> depositIds = new ArrayList<>();
        List<Long> savingIds = new ArrayList<>();

        Type type = filter.type();

        for(Comparison product : products){
            // 필터 없이 전체 검색
            if (type == null) {
                switch (product.getType()) {
                    case CARD -> cardIds.add(product.getProductId());
                    case DEPOSIT -> depositIds.add(product.getProductId());
                    case SAVING -> savingIds.add(product.getProductId());
                }
                continue;
            }

            // 필터 있음 → 해당 타입만 분류
            if (product.getType() == type) {
                switch (type) {
                    case CARD -> cardIds.add(product.getProductId());
                    case DEPOSIT -> depositIds.add(product.getProductId());
                    case SAVING -> savingIds.add(product.getProductId());
                }
            }
        }

        // 3. 필터
        return new ProductFilterRes(
                searchCards(filter, cardIds),
                searchDeposits(filter, depositIds),
                searchSavings(filter, savingIds)
        );
    }

    private void validateProductExists(Type type, Long productId) {
        switch (type) {
            case CARD -> cardRepo.findById(productId).orElseThrow(NotFoundCardException::new);
            case DEPOSIT -> depositRepo.findById(productId).orElseThrow(NotFoundDepositException::new);
            case SAVING -> savingRepo.findById(productId).orElseThrow(NotFoundSavingException::new);
        }
    }

    // 카드 상품 필터링 검색
    private List<ProductFilterRes.CardProductRes> searchCards(ProductFilterReq filter, List<Long> cardIds) {
        return cardRepo.searchProducts(filter, cardIds).stream()
                .map(ProductFilterRes.CardProductRes::fromCard)
                .toList();
    }

    // 예금 상품 필터링 검색
    private List<ProductFilterRes.DepositProductRes> searchDeposits(ProductFilterReq filter, List<Long> depositIds) {
        return depositRepo.searchProducts(filter, depositIds).stream()
                .map(ProductFilterRes.DepositProductRes::fromDeposit)
                .toList();
    }

    // 적금 상품 필터링 검색
    private List<ProductFilterRes.SavingProductRes> searchSavings(ProductFilterReq filter, List<Long> savingIds){
        return savingRepo.searchProducts(filter, savingIds).stream()
                .map(ProductFilterRes.SavingProductRes::fromSaving)
                .toList();
    }
}
