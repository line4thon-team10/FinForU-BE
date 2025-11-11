package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "card_name_en", nullable = false)
    private String nameEn;

    @Column(name = "card_name_zh", nullable = false)
    private String nameZh;

    @Column(name = "card_name_vi", nullable = false)
    private String nameVi;

    // 특징 정보
    @Column(name = "description_en", nullable = false)
    private String descriptionEn;

    @Column(name = "description_zh", nullable = false)
    private String descriptionZh;

    @Column(name = "description_vi", nullable = false)
    private String descriptionVi;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    // 연회비
    @Column(name = "domestic_annual_fee", nullable = false)
    private int domesticAnnualFee;

    @Column(name = "international_annual_fee")
    private int internationalAnnualFee;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @Builder.Default
    @OneToMany(mappedBy = "card",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardBenefit> cardBenefit = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id",nullable = false)
    private Bank bank;


    public String getNameByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "zh" -> this.nameZh;
            case "vi" -> this.nameVi;
            default -> this.nameEn;
        };
    }

    public String getDescriptionByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "zh" -> this.descriptionZh;
            case "vi" -> this.descriptionVi;
            default -> this.descriptionEn;
        };
    }
}
