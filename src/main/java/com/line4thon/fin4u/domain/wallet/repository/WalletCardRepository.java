package com.line4thon.fin4u.domain.wallet.repository;

import com.line4thon.fin4u.domain.wallet.entity.WalletCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletCardRepository extends JpaRepository<WalletCard, Long> {
}
