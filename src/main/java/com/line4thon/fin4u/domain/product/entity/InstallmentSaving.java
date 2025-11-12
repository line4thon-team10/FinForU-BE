package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "saving_product")
public class InstallmentSaving extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_id")
    private Long id;

    @Column(name = "saving_name_en", nullable = false)
    private String nameEn;

    @Column(name = "saving_name_zh", nullable = false)
    private String nameZh;

    @Column(name = "saving_name_vi", nullable = false)
    private String nameVi;

    // 특징 정보
    @Column(name = "description_en", nullable = false)
    private String descriptionEn;

    @Column(name = "description_zh", nullable = false)
    private String descriptionZh;

    @Column(name = "description_vi", nullable = false)
    private String descriptionVi;

    // 기본금리
    @Column(name = "base_interest_rate", nullable = false)
    private double baseInterestRate;

    // 우대금리
    @Column(name = "max_interest_rate", nullable = false)
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

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    public String getNameByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "en" -> this.nameEn;
            case "zh" -> this.nameZh;
            case "vi" -> this.nameVi;
            default -> this.nameEn; // 기본값은 한국어
        };
    }

    public String getDescriptionByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "en" -> this.descriptionEn;
            case "zh" -> this.descriptionZh;
            case "vi" -> this.descriptionVi;
            default -> this.descriptionEn; // 기본값은 한국어
        };
    }


}
