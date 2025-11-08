package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.product.entity.enums.PaymentMethod;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InstallmentSaving extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @Column(name = "deposit_name", nullable = false)
    private String name;

    // 기본금리
    @Column(name = "base_interest_rate", nullable = false)
    private double baseInterestRate;

    // 우대금리
    @Column(name = "max_interest_rate")
    private double maxInterestRate;

    // 납입 방식 (월 / 자유)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    // 월 최소 납입금액
    @Column(name = "min_monthly")
    private int minMonthly;

    // 월 최대 납입금액
    @Column(name = "max_monthly")
    private int maxMonthly;

    // 가입 기간
    @Column(name = "saving_term", nullable = false)
    private Integer savingTerm;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;


}
