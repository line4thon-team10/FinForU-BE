package com.line4thon.fin4u.domain.recommend.fastApi.service;

import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.recommend.fastApi.AiRecClient;
import com.line4thon.fin4u.domain.recommend.fastApi.EmbedTextBuilder;
import com.line4thon.fin4u.domain.recommend.fastApi.dto.UpsertRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpsertServiceImpl implements UpsertService {

    private final CardRepository cardRepo;
    private final DepositRepository depositRepo;
    private final InstallmentSavingRepository savingRepo;
    private final AiRecClient aiRecClient;

    public void upsertAllProducts() {

        List<UpsertRes.UpsertItem> items = new ArrayList<>();
        items.addAll(buildCardItems());
        items.addAll(buildDepositItems());
        items.addAll(buildSavingItems());

        aiRecClient.postUpsert(items);

    }

    /**
     * 벡터 db에 저장할 구체적 전송 내용(upsert)
     */

    //카드 형식 리스트
    private List<UpsertRes.UpsertItem> buildCardItems() {
        return cardRepo.findAll().stream().map(c ->
                UpsertRes.UpsertItem.builder()
                        .id(String.valueOf(c.getId()))
                        .type("CARD")
                        .text_for_embed(EmbedTextBuilder.cardText(c))

                        .bank(c.getBank().getBankName())

                        .annual_fee_domestic(c.getDomesticAnnualFee())
                        .annual_fee_international(c.getInternationalAnnualFee())
                        .card_type(c.getCardType().name())
                        .benefit_tags(c.getCardBenefit().stream()
                                .map(b -> b.getBenefitCategory().name())
                                .toList())
                        .build()
        ).toList();
    }

    //예금 형식 리스트
    private List<UpsertRes.UpsertItem> buildDepositItems() {
        return depositRepo.findAll().stream().map(d ->
                UpsertRes.UpsertItem.builder()
                        .id(String.valueOf(d.getId()))
                        .type("DEPOSIT")
                        .text_for_embed(EmbedTextBuilder.depositText(d))

                        .bank(d.getBank().getBankName())

                        .term(EmbedTextBuilder.term(d.getDepositTerm()))
                        .base_rate(d.getBaseInterestRate())
                        .max_rate(d.getMaxInterestRate())
                        .build()
        ).toList();
    }

    //적금 형식 리스트
    private List<UpsertRes.UpsertItem> buildSavingItems() {
        return savingRepo.findAll().stream().map(s ->
                UpsertRes.UpsertItem.builder()
                        .id(String.valueOf(s.getId()))
                        .type("SAVING")
                        .text_for_embed(EmbedTextBuilder.savingText(s))
                        .bank(s.getBank().getBankName())

                        .term(EmbedTextBuilder.term(s.getSavingTerm()))
                        .base_rate(s.getBaseInterestRate())
                        .max_rate(s.getMaxInterestRate())
                        .build()
        ).toList();
    }

}
