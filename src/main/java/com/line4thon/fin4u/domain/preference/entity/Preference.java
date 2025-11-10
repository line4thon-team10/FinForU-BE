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

    @ElementCollection(fetch = FetchType.LAZY) // 컬렉션 선언
    @CollectionTable( // 중간테이블 생성
            name = "member_pref_product_type",
            joinColumns = @JoinColumn(name = "pref_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "pref_type")
    private List<Type> preferProductTypes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable( // 중간테이블 생성
            name = "member_savings_goal_period",
            joinColumns = @JoinColumn(name = "pref_id"))
    @Enumerated(EnumType.STRING) // Enum 이름을 DB에 문자열로 저장
    @Column(name = "goal_period")
    private List<SavingGoalPeriod> savingGoalPeriods = new ArrayList<>();

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
    @Column(name = "bank_purpose", nullable = false)
    private Bank bank;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Preference of(Member member, SavePreferReq req){
        return Preference.builder()
                .member(member)
                .savingPurpose(req.getSavingPurpose())
                .cardPurpose(req.getCardPurpose())
                .income(req.getIncome())
                .bank(req.getBank())

                .preferProductTypes(req.getTypes())
                .savingGoalPeriods(req.getPeriods())
                .build();
    }

    public void update(SavePreferReq req) {
        this.savingPurpose = req.getSavingPurpose();
        this.cardPurpose = req.getCardPurpose();
        this.income = req.getIncome();
        this.bank = req.getBank();

        if (req.getTypes() != null) {
            this.preferProductTypes.clear(); // 기존 DB 데이터 삭제
            this.preferProductTypes.addAll(req.getTypes()); // 새 데이터 추가
        }

        if(req.getPeriods() != null){
            this.savingGoalPeriods.clear();
            this.savingGoalPeriods.addAll(req.getPeriods());
        }
    }
}
