package com.line4thon.fin4u.domain.product.repository;

import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.repository.custom.SavingCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentSavingRepository extends JpaRepository<InstallmentSaving, Long>, SavingCustomRepository {
}
