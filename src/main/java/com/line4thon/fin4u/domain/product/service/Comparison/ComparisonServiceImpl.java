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
import java.util.Comparator;
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

    // 상품 비교
    @Override
    public CompareRes compare(List<Long> productIds, Type type) {
        // 상품 검증
        productIds.forEach(id -> validateProductExists(type, id));

        return switch(type){
            case CARD -> compareCard(productIds);
            case DEPOSIT -> compareDeposit(productIds);
            case SAVING -> compareSaving(productIds);
        };
    }

    // 카드 비교
    private CompareRes compareCard(List<Long> productIds) {

        List<Card> cards = cardRepo.findAllById(productIds);

        if(cards.size() != productIds.size())
            throw new NotFoundSavingException();

        // 국내 연회비
        Long lowestDomesticId = cards.stream()
                .min(Comparator.comparing(Card::getDomesticAnnualFee))
                .map(Card::getId)
                .orElse(null); //동일하면 null

        // 국외 연회비
        Long lowestInternationalId = cards.stream()
                .min(Comparator.comparing(Card::getInternationalAnnualFee))
                .map(Card::getId)
                .orElse(null); //동일시 null


        // 반환
        List<CompareRes.CardCompareRes> result = cards.stream()
                .map(CompareRes.CardCompareRes::fromCard)
                .toList();

        CompareRes.Highlights highlight = CompareRes.cardHighlight(lowestDomesticId, lowestInternationalId);

        return new CompareRes(
            result, null, null,
                highlight
        );
    }

    // 예금 비교
    private CompareRes compareDeposit(List<Long> productIds) {

        List<Deposit> deposits= depositRepo.findAllById(productIds);

        if(deposits.size() != productIds.size())
            throw new NotFoundDepositException();

        //기본 금리
        Long bestBaseRateId = deposits.stream()
                .max(Comparator.comparing(Deposit::getBaseInterestRate))
                .map(Deposit::getId)
                .orElse(null); //동일시 null

        //최대 금리
        Long bestMaxRateId = deposits.stream()
                .max(Comparator.comparing(Deposit::getMaxInterestRate))
                .map(Deposit::getId)
                .orElse(null);

        //월 납입한도
        Long lowestMinDepositId = deposits.stream()
                .min(Comparator.comparing(Deposit::getMinDepositAmount))
                .map(Deposit::getId)
                .orElse(null);

        //반환
        List<CompareRes.DepositCompareRes> result = deposits.stream()
                        .map(CompareRes.DepositCompareRes::fromDeposit)
                        .toList();

        CompareRes.Highlights highlight= CompareRes.depositHighlight(bestBaseRateId, bestMaxRateId, lowestMinDepositId);

        return new CompareRes(
                null, result, null,
                highlight
        );

    }

    // 적금 비교
    private CompareRes compareSaving(List<Long> productIds) {

        List<InstallmentSaving> savings = savingRepo.findAllById(productIds);

        if (savings.size() != productIds.size())
            throw new NotFoundSavingException();

        //기본 금리
        Long bestBaseRateId = savings.stream()
                .max(Comparator.comparing(InstallmentSaving::getBaseInterestRate))
                .map(InstallmentSaving::getId)
                .orElse(null);

        //최대 금리
        Long bestMaxRateId = savings.stream()
                .max(Comparator.comparing(InstallmentSaving::getMaxInterestRate))
                .map(InstallmentSaving::getId)
                .orElse(null);

        //월 납입 한도
        Long lowestMaxMonthly = savings.stream()
                .min(Comparator.comparing(InstallmentSaving::getMaxMonthly))
                .map(InstallmentSaving::getId)
                .orElse(null);

        //반환
        List<CompareRes.SavingCompareRes> result = savings.stream()
                .map(CompareRes.SavingCompareRes::fromSaving)
                .toList();

        CompareRes.Highlights highlight = CompareRes.savingHighlight(bestBaseRateId, bestMaxRateId, lowestMaxMonthly);

        return new CompareRes(
                null,null, result,
                highlight
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
