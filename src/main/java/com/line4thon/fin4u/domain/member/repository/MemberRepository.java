package com.line4thon.fin4u.domain.member.repository;

import com.line4thon.fin4u.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);

}
