package com.line4thon.fin4u.domain.product.repository;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.entity.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparisonRepository extends JpaRepository<Comparison, Long> {
    List<Comparison> findByGuestToken(String guestToken);

    List<Comparison> findByMember(Member member);
}
