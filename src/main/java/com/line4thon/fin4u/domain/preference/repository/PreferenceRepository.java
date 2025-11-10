package com.line4thon.fin4u.domain.preference.repository;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.preference.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByMember(Member member);

    Optional<Preference> findByMemberId(Long attr0);
}
