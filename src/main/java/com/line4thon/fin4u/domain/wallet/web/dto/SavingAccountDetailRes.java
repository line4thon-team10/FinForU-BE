package com.line4thon.fin4u.domain.wallet.web.dto;

import java.time.LocalDate;

public record SavingAccountDetailRes(
        String bank,
        String savingType,
        String savingName,
        Integer monthlyPay,
        Integer paymentDate,
        LocalDate startDate,
        LocalDate endDate
) {
}
