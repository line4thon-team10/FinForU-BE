package com.line4thon.fin4u.domain.preference.web.dto;

import com.line4thon.fin4u.domain.preference.entity.Preference;
import com.line4thon.fin4u.domain.preference.entity.enums.*;
import com.line4thon.fin4u.domain.product.entity.enums.Type;

import java.util.List;

public record SavePreferRes(
        List<Type> types,
        List<SavingGoalPeriod> periods,
        SavingPurpose savingPurpose,
        CardPurpose cardPurpose,
        Income income,
        Bank bank
) {
    public static SavePreferRes from(Preference preference){
        return new SavePreferRes(
                preference.getPreferProductTypes(),
                preference.getSavingGoalPeriods(),
                preference.getSavingPurpose(),
                preference.getCardPurpose(),
                preference.getIncome(),
                preference.getBank()
        );

    }

    public static SavePreferRes defaultResponse() {
        return new SavePreferRes(
                List.of(),
                List.of(),
                null,
                null,
                null,
                null
        );
    }
}
