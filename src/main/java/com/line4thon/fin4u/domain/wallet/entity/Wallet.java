package com.line4thon.fin4u.domain.wallet.entity;

import com.line4thon.fin4u.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="wallet_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable=false)
    private Member member;
}
