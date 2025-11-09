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

    @Column(name = "card_name", nullable = false)
    private String name;

    // 특징 정보
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    // 연회비
    @Column(name = "domestic_annual_fee")
    private int domesticAnnualFee;

    @Column(name = "international_annual_fee")
    private int internationalAnnualFee;

    // 나이 자격
    @Column(name = "min_age")
    private Integer minAge;

    // 공식 사이트 링크
    @Column(name = "website")
    private String officialWebsite;

    @OneToMany(mappedBy = "card")
    private List<CardBenefit> cardBenefit = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
