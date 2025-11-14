package com.line4thon.fin4u.domain.recommend.service;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.preference.entity.Preference;
import com.line4thon.fin4u.domain.preference.exception.NotFoundPreferenceException;
import com.line4thon.fin4u.domain.preference.repository.PreferenceRepository;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.exception.NotFoundCardException;
import com.line4thon.fin4u.domain.product.exception.NotFoundDepositException;
import com.line4thon.fin4u.domain.product.exception.NotFoundSavingException;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.domain.recommend.fastApi.AiRecClient;
import com.line4thon.fin4u.domain.recommend.fastApi.dto.AiRecommendRes;
import com.line4thon.fin4u.domain.recommend.fastApi.dto.AiRecommendReq;
import com.line4thon.fin4u.domain.recommend.web.dto.RecommendRes;
import com.line4thon.fin4u.global.util.BankNameTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final BankNameTranslator translator;

    private final MemberRepository memberRepo;
    private final PreferenceRepository preferRepo;
    private final AiRecClient ai;

    private final CardRepository cardRepo;
    private final DepositRepository depositRepo;
    private final InstallmentSavingRepository savingRepo;

    //상품 추천
    @Override
    public RecommendRes recommend(Principal principal, String langCode) {

        // 멤버 존재 확인
        Member member = checkMember(principal);

        // Preference 조회
        Preference prefer = preferRepo.findByMember(member);
        if(prefer == null)
            throw new NotFoundPreferenceException();

        // Enum-> 문자열로 변환
        AiRecommendReq req = toPrefDto(prefer);

        // 파이썬 AI 서버에 추천 요청
        AiRecommendRes aiRes = ai.recommend(req);

        if(aiRes==null)
            return new RecommendRes(List.of());

        return mappingType(aiRes, langCode);
    }

    private RecommendRes mappingType(AiRecommendRes aiRes, String langCode) {

        List<RecommendRes.ResultItem> results = new ArrayList<>();

        for(AiRecommendRes.AiRecItem item : aiRes.results()){

            String type = item.type();
            Long id = item.id();

            switch (type) {
                case "CARD" -> {
                    Card card = cardRepo.findById(id)
                            .orElseThrow(NotFoundCardException::new);
                    results.add(RecommendRes.CardItem.fromCard(
                            card,
                            langCode,
                            translator
                    ));
                }
                case "DEPOSIT" -> {
                    Deposit dep = depositRepo.findById(id)
                            .orElseThrow(NotFoundDepositException::new);

                    results.add(RecommendRes.DepositItem.fromDeposit(
                            dep,
                            langCode,
                            translator
                    ));
                }
                case "SAVING" -> {
                    InstallmentSaving sav = savingRepo.findById(id)
                            .orElseThrow(NotFoundSavingException::new);

                    results.add(RecommendRes.SavingItem.fromSaving(
                            sav,
                            langCode,
                            translator
                    ));
                }
            }

        }

        return new RecommendRes(results);
    }

    //멤버 검증
    private Member checkMember(Principal principal){
        String email = (principal != null) ? principal.getName() : null;

        if (email == null || email.isBlank()) {
            throw new MemberNotFoundException();
        }

        return memberRepo.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }


    //선호도를 파이썬으로 보낼 String으로 변환하기
    private AiRecommendReq toPrefDto(Preference prefer) {

        //사용자 선호 금융 상품 enum -> string으로 가져오기
        List<String> types = prefer.getPreferProductTypes().stream()
                .map(type -> type.getType().name())
                .toList();

        //사용자 예적금 기간 enum -> string으로 가져오기
        List<String> periods = prefer.getSavingGoalPeriods().stream()
                .map(term -> term.getPeriod().name().toLowerCase())
                .toList();

        return new AiRecommendReq(
                types,
                periods,
                prefer.getSavingPurpose() == null ? null : prefer.getSavingPurpose().name(),
                prefer.getCardPurpose() == null ? null : prefer.getCardPurpose().name(),
                prefer.getBank() == null ? null : prefer.getBank().name(), //null값 가능
                prefer.getIncome() == null ? null : prefer.getIncome().name()
        );
    }

}
