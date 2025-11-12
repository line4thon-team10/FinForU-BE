package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.product.entity.enums.BenefitCategory;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CardBenefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_benefits_id")
    private Long id;

    @Column(name = "is_promotional", nullable = false)
    private boolean isPromotional;

    @Column(name = "category")
    private String  benefitCategory;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "description_zh")
    private String descriptionZh;

    @Column(name = "description_vi")
    private String descriptionVi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public String getDescriptionByLang(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "zh" -> this.descriptionZh;
            case "vi" -> this.descriptionVi;
            default -> this.descriptionEn;
        };
    }

}
