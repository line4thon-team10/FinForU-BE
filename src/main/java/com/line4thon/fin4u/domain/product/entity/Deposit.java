package com.line4thon.fin4u.domain.product.entity;

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

    // 특징 정보
    @Column(name = "description", nullable = false)
    private String description;

    // 기본금리
    @Column(name = "base_interest_rate", nullable = false)
    private double baseInterestRate;

    // 최고 우대금리
    @Column(name = "max_interest_rate")
    private double maxInterestRate;

    // 예치 기간
    @Column(name = "deposit_term")
    private Integer depositTerm;

    // 예치 기간 유연성
    @Column(name = "flexible", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFlexible;

    // 최소 예치 금액
    @Column(name = "min_deposit_amount", nullable = false)
    private Integer minDepositAmount;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
