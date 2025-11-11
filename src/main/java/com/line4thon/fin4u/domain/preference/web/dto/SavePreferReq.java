package com.line4thon.fin4u.domain.preference.web.dto;

import com.line4thon.fin4u.domain.preference.entity.enums.*;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SavePreferReq {
    @NotNull(message = "상품 유형(type)은 필수 값입니다.")
    List<Type> types;

    @NotNull(message = "적금 목표 기간은 필수 값입니다.")
    List<SavingGoalPeriod> periods;

    @NotNull(message = "적금 목표는 필수 값입니다.")
    SavingPurpose savingPurpose;

    @NotNull(message = "카드 목표는 필수 값입니다.")
    CardPurpose cardPurpose;

    @NotNull(message = "월 수입은 필수 값입니다.")
    Income income;

    Bank bank;
}
