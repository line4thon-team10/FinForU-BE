package com.line4thon.fin4u.domain.preference.entity;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.preference.entity.enums.*;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferReq;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Preference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pref_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "saving_purpose", nullable = false)
    private SavingPurpose savingPurpose;

    @Enumerated(EnumType.STRING)
    @Column(name ="card_purpose", nullable = false)
    private CardPurpose cardPurpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "income", nullable = false)
    private Income income;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank")
    private Bank bank;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "preference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTypePreference> preferProductTypes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "preference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingGoalPeriodPreference> savingGoalPeriods = new ArrayList<>();


    public static Preference of(Member member, SavePreferReq req){
        Preference p = Preference.builder()
                .member(member)
                .savingPurpose(req.getSavingPurpose())
                .cardPurpose(req.getCardPurpose())
                .income(req.getIncome())
                .bank(req.getBank())
                .build();

        // Enum -> 엔티티 변환
        req.getTypes().forEach(type ->
                p.preferProductTypes.add(new ProductTypePreference(type, p))
        );

        req.getPeriods().forEach(period ->
                p.savingGoalPeriods.add(new SavingGoalPeriodPreference(period, p))
        );

        return p;

    }

    public void update(SavePreferReq req) {
        this.savingPurpose = req.getSavingPurpose();
        this.cardPurpose = req.getCardPurpose();
        this.income = req.getIncome();
        this.bank = req.getBank();

        this.preferProductTypes.clear();
        this.savingGoalPeriods.clear();

        req.getTypes().forEach(type ->
                this.preferProductTypes.add(new ProductTypePreference(type, this))
        );

        req.getPeriods().forEach(period ->
                this.savingGoalPeriods.add(new SavingGoalPeriodPreference(period, this))
        );
    }
}
