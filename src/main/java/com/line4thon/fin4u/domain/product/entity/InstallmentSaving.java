package com.line4thon.fin4u.domain.product.entity;

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

    // 특징 정보
    @Column(name = "description", nullable = false)
    private String description;

    // 기본금리
    @Column(name = "base_interest_rate", nullable = false)
    private double baseInterestRate;

    // 우대금리
    @Column(name = "max_interest_rate")
    private double maxInterestRate;

    // 가입 기간
    @Column(name = "saving_term")
    private Integer savingTerm;

    // 가입 기간 유연성
    @Column(name = "flexible", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFlexible;

    // 월 최대 납입금액
    @Column(name = "max_monthly", nullable = false)
    private int maxMonthly;

    // 나이 자격
    @Column(name = "min_age")
    private Integer minAge;

    // valid ID (신분증 필수 여부)
    @Column(name = "id_required", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean idRequired;

    // 거주자 여부
    @Column(name = "is_resident", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isResident;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;


}
