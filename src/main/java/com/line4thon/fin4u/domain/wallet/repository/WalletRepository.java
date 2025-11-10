package com.line4thon.fin4u.domain.wallet.repository;

import com.line4thon.fin4u.domain.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByMemberMemberId(Long memberId);
}
