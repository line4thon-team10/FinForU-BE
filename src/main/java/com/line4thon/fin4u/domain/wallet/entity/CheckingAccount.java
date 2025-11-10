package com.line4thon.fin4u.domain.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String checkingAccountName;
}
