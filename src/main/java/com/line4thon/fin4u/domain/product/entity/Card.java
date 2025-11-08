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

    // 특징 정보
    @Column(name = "description", nullable = false)
    private String description;

    // 연회비
    @Column(name = "domestic_annual_fee")
    private int domesticAnnualFee;

    @Column(name = "international_annual_fee")
    private int internationalAnnualFee;

    // 나이 자격
    @Column(name = "min_age")
    private Integer minAge;

    // valid ID (신분증 필수 여부)
    @Column(name = "id_required", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean idRequired;

    // 거주자 여부
    @Column(name = "is_resident", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isResident;

    //카드 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
