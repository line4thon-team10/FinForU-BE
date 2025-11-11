package com.line4thon.fin4u.domain.wallet.entity;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Account;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccount extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank bank;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Account savingType;

    @Column(nullable = false)
    private String savingName;

    private Integer monthlyPay;

    private Integer paymentDate;

    // 테이블을 나눌 필요가 없는 것 같아 한번에 작성
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    public void modify(
            Bank bank,
            Account productType,
            String productName,
            LocalDate startDate,
            LocalDate endDate,
            Integer monthlyPay,
            Integer paymentDate
    ) {
        this.bank = bank;
        this.savingType = productType;
        this.savingName = productName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPay = monthlyPay;
        this.paymentDate = paymentDate;

    }
}
