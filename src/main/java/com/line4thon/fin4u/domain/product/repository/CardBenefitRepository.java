package com.line4thon.fin4u.domain.product.repository;

import com.line4thon.fin4u.domain.product.entity.CardBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {
    List<CardBenefit> findByCardId(Long id);
}
