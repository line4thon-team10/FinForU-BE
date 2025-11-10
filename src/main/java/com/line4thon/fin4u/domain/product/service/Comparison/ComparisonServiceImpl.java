package com.line4thon.fin4u.domain.product.service.Comparison;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Comparison;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.exception.NotFoundCardException;
import com.line4thon.fin4u.domain.product.exception.NotFoundDepositException;
import com.line4thon.fin4u.domain.product.exception.NotFoundGuestTokenException;
import com.line4thon.fin4u.domain.product.exception.NotFoundSavingException;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.ComparisonRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.CompareRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    public void saveProduct(Principal principal, String guestToken, Type type, Long productId) {
        // 존재 상품인지 확인
        validateProductExists(type, productId);

        // 회원, 비회원 검증
        UserKey user = checkMember(principal, guestToken);

        Comparison comparison = (user.member() != null)
                ? Comparison.of(user.member(), type, productId)
                : Comparison.ofGuest(user.guestToken(), type, productId);

        comparisonRepo.save(comparison);

    }

    // 바구니 조회&필터링
    @Override
    public ProductFilterRes getComparisonFilter(Principal principal, String guestToken, ProductFilterReq filter) {
        // 회원, 비회원 검증
        UserKey user = checkMember(principal, guestToken);

        // 1. 각 바구니
        List<Comparison> products = (user.member() != null)
                ? comparisonRepo.findByMember(user.member())
                : comparisonRepo.findByGuestToken(guestToken);

        // 2. 타입별 분류
        List<Long> cardIds = new ArrayList<>();
        List<Long> depositIds = new ArrayList<>();
        List<Long> savingIds = new ArrayList<>();

        Type type = filter.type();

        for (Comparison product : products) {
            if (type == null || product.getType() == type) {
                switch (product.getType()) {
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


    private record UserKey(Member member, String guestToken) {}

    // 회원, 비회원 검증
    private UserKey checkMember(Principal principal, String guestToken){
        String email = (principal != null) ? principal.getName() : null;

        if(email != null) {
            Member member =  memberRepo.findByEmail(email)
                    .orElseThrow(MemberNotFoundException::new);
            return new UserKey(member, null);
        }

        if(guestToken != null && !guestToken.isBlank())
            return new UserKey(null, guestToken);

        throw new NotFoundGuestTokenException();
    }


    // 상품 존재 확인
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
