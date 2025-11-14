package com.line4thon.fin4u.domain.member.service;


import com.line4thon.fin4u.domain.member.web.dto.request.UpdateMemberRequest;
import com.line4thon.fin4u.domain.member.web.dto.response.MemberResponse;
import com.line4thon.fin4u.domain.member.entity.Member;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse editMe(String email, UpdateMemberRequest req) {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (req.getNationality() != null) m.setNationality(req.getNationality());
        if (req.getLanguage() != null) m.setLanguage(req.getLanguage());
        if (req.getVisaType() != null) m.setVisaType(req.getVisaType());
        if (req.getVisaExpir() != null) m.setVisa_expir(req.getVisaExpir());
        if (req.getNotify() != null) m.setNotify(req.getNotify());
        if (req.getDesiredProductTypes() != null) {
            if (req.getDesiredProductTypes().isEmpty()) {
                m.setDesiredProductTypes(new HashSet<>(Set.of(Member.DesiredProductType.CARD)));
            } else {
                m.setDesiredProductTypes(new HashSet<>(req.getDesiredProductTypes()));
            }
        }

        m.setUpdated_at(Timestamp.from(Instant.now()));

        // 여기서는 save() 안 해도 됨 (영속 상태 + @Transactional 이라 dirty checking 됨)
        return MemberResponse.from(m);
    }
}

