package com.line4thon.fin4u.domain.product.repository;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.repository.custom.DepositCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository가 제공해야 하는 모든 기능의 목록(규칙)을 정의
@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>, DepositCustomRepository {
}
