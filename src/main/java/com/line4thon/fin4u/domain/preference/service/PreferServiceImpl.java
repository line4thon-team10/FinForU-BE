package com.line4thon.fin4u.domain.preference.service;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.preference.entity.Preference;
import com.line4thon.fin4u.domain.preference.exception.NotFoundPreferenceException;
import com.line4thon.fin4u.domain.preference.repository.PreferenceRepository;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferReq;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PreferServiceImpl implements PreferService{

    private final PreferenceRepository preferRepo;
    private final MemberRepository memberRepo;

    //저장
    @Override
    public SavePreferRes savePrefer(Principal principal, SavePreferReq req) {
        // 멤버 검증
        Member member = checkMember(principal);

        // Preference 조회 / 새로 생성
        Preference prefer = preferRepo.findByMemberId(member.getMemberId())
                .orElseGet(() -> Preference.of(member, req));

        //업데이트
        prefer.update(req);

        //저장
        Preference saved = preferRepo.save(prefer);

        return SavePreferRes.from(saved);
    }

    //조회
    @Override
    public SavePreferRes getPrefer(Principal principal) {
        // 멤버 검증
        Member member = checkMember(principal);

        // Preference 조회
        Preference prefer = preferRepo.findByMember(member)
                .orElseThrow(NotFoundPreferenceException::new);

        return SavePreferRes.from(prefer);
    }


    private Member checkMember(Principal principal){
        String email = (principal != null) ? principal.getName() : null;

        return memberRepo.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
