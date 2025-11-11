package com.line4thon.fin4u.domain.guide;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends IntegrationTestSupport {


    @Autowired
    MemberRepository memberRepository;

    @Test
    void testDB() {
        Member m = memberRepository.save(
                Member.builder()
                        .language(Member.Language.ENGLISH)
                        .email("user1@test.com")
                        .password("1234")
                        .name("user1")
                        .nationality("KOREA")
                        .visaType(Member.VisaType.ACCOUNT_OPEN)
                        .visa_expir(Timestamp.from(Instant.now()))
                        .notify(true)
                        .build()
        );
        assertThat(memberRepository.findById(m.getMemberId())).isPresent();
    }
}