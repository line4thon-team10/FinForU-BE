package com.line4thon.fin4u.domain.wallet.entity;

import com.line4thon.fin4u.domain.wallet.entity.enumulate.Bank;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private Bank bank;

    public void modify(Bank bank) {
        this.bank = bank;
    }
}
