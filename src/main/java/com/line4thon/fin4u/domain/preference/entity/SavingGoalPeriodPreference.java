package com.line4thon.fin4u.domain.preference.entity;
import com.line4thon.fin4u.domain.preference.entity.enums.SavingGoalPeriod;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingGoalPeriodPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private SavingGoalPeriod period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pref_id")
    private Preference preference;

    public SavingGoalPeriodPreference(SavingGoalPeriod period, Preference preference) {
        this.period = period;
        this.preference = preference;
    }


}
