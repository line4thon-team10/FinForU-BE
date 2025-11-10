package com.line4thon.fin4u.domain.wallet.entity.enumulate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bank {
    KOOKMIN("Kookmin"),
    HANA("Hana"),
    SHINHAN("Shinhan"),
    WOORI("Woori"),;

    private String lower;
}
