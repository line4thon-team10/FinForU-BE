package com.line4thon.fin4u.domain.product.repository;

import com.line4thon.fin4u.domain.product.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}
