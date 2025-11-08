package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.product.entity.enums.InterestPayment;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "deposit_product")
public class Deposit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @Column(name = "deposit_name", nullable = false)
    private String name;

    // 기본금리
    @Column(name = "base_interest_rate", nullable = false)
    private double baseInterestRate;

    // 최고 우대금리
    @Column(name = "max_interest_rate")
    private double maxInterestRate;

    // 최소 가입 금액 (목돈 거치 시 필요한 최소 금액)
    @Column(name = "min_deposit_amount", nullable = false)
    private Integer minDepositAmount;

    // 예치 기간
    @Column(name = "deposit_term", nullable = false)
    private Integer depositTerm;

    // 이자 지급 방식 (만기 일시 지급, 월 복리)
    @Enumerated(EnumType.STRING)
    @Column(name = "interest_payment")
    private InterestPayment interestPayment;

    // 혜택
    @Column(name = "card_benefits")
    private String cardBenefits;

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
