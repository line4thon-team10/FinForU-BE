package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "card_product")
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long id;

    @Column(name = "card_name", nullable = false)
    private String name;

    // 혜택 실적 조건
    @Column(name = "min_amound", nullable = false)
    private int minAmount;

    // 혜택 한도

    //카드 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    // 혜택
    @Column(name = "card_benefits")
    private String cardBenefits;

    // 연회비
    @Column(name = "annual_fee")
    private int annualFee;

    // 특징/부가 정보
    @Column(name = "features")
    private String features;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
