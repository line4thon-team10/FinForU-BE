package com.line4thon.fin4u.domain.wallet.repository;

import com.line4thon.fin4u.domain.wallet.entity.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
}
