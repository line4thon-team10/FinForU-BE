package com.line4thon.fin4u.domain.member.service;

import com.line4thon.fin4u.domain.member.web.dto.request.LoginRequest;
import com.line4thon.fin4u.domain.member.web.dto.request.SignupRequest;
import com.line4thon.fin4u.domain.member.web.dto.response.AuthResponse;
import com.line4thon.fin4u.domain.member.web.dto.response.MemberResponse;
import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.exception.DuplicateEmailException;
import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.global.security.JwtProvider;
import com.line4thon.fin4u.global.web.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.line4thon.fin4u.domain.member.web.dto.request.DeleteAccountRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Value("${jwt.access-exp-minutes}") private long accessExpMinutes; // 분
    @Value("${jwt.refresh-exp-days}")  private long refreshExpDays;    // 일

    /**
     * 회원가입
     * - 이메일 중복 시 DuplicateEmailException
     * - 성공 시 HttpOnly 쿠키에 ACCESS/REFRESH 토큰 세팅
     */
    @Transactional
    public AuthResponse signup(SignupRequest req, HttpServletResponse res) {
        if (memberRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException();
        }

        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .nationality(req.getNationality())
                .language(req.getLanguage())
                .visaType(req.getVisaType())
                .desiredProductTypes(
                        (req.getDesiredProductTypes() == null || req.getDesiredProductTypes().isEmpty())
                                ? new HashSet<>(Set.of(Member.DesiredProductType.CARD))
                                : new HashSet<>(req.getDesiredProductTypes())
                )
                .visa_expir(req.getVisaExpir())
                .notify(req.getNotify() == null ? true : req.getNotify())
                .created_at(Timestamp.from(Instant.now()))
                .updated_at(Timestamp.from(Instant.now()))
                .build();

        Member saved = memberRepository.save(member);

        issueTokens(saved, res);

        return AuthResponse.builder()
                .member(MemberResponse.from(saved))
                .message("signup success")
                .build();
    }

    /**
     * 로그인
     * - 이메일 미존재 또는 비밀번호 불일치: MemberNotFoundException (사용자 식별 방지)
     * - 성공 시 HttpOnly 쿠키에 ACCESS/REFRESH 토큰 세팅
     */
    @Transactional
    public AuthResponse login(LoginRequest req, HttpServletResponse res) {
        Member m = memberRepository.findByEmail(req.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(req.getPassword(), m.getPassword())) {
            throw new MemberNotFoundException();
        }

        issueTokens(m, res);

        return AuthResponse.builder()
                .member(MemberResponse.from(m))
                .message("login success")
                .build();
    }

    /**
     * 액세스 토큰 재발급
     * - refresh 유효성 실패/계정 없음: MemberNotFoundException
     * - 성공 시 ACCESS 토큰만 재발급(쿠키 갱신)
     */
    @Transactional
    public void reissueAccess(String refreshToken, HttpServletResponse res) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new MemberNotFoundException();
        }
        String email = jwtProvider.getSubject(refreshToken);

        Member m = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        String newAccess = jwtProvider.generateAccess(email, buildAccessClaims(m));
        cookieUtil.addAccessToken(res, newAccess, (int)(accessExpMinutes * 60));
    }

    /** 공용: 액세스/리프레시 둘 다 발급 + 쿠키 세팅 */
    private void issueTokens(Member m, HttpServletResponse res) {
        String access = jwtProvider.generateAccess(m.getEmail(), buildAccessClaims(m));
        String refresh = jwtProvider.generateRefresh(m.getEmail());

        cookieUtil.addAccessToken(res, access, (int)(accessExpMinutes * 60));
        cookieUtil.addRefreshToken(res, refresh, (int)(refreshExpDays * 24 * 60 * 60));
    }

    /** 액세스 토큰 클레임 구성 (다중 desiredProductTypes 반영) */
    private Map<String, Object> buildAccessClaims(Member m) {
        var types = (m.getDesiredProductTypes() == null || m.getDesiredProductTypes().isEmpty())
                ? Collections.singleton(Member.DesiredProductType.CARD) // 안전 기본값
                : m.getDesiredProductTypes();

        var products = types.stream().map(Enum::name).collect(Collectors.toList());

        return Map.of(
                "name", m.getName(),
                "lang", m.getLanguage().name(),
                "visa", m.getVisaType().name(),
                "products", products // ✅ 배열로 저장
        );
    }

    /** 로그아웃: HttpOnly 토큰 쿠키 삭제 */
    public void logout(HttpServletResponse res) {
        cookieUtil.clearTokens(res);
    }

    /**
     * 회원 탈퇴(하드 삭제)
     * - 인증된 이메일을 기준으로 본인 계정 삭제
     * - (옵션) 비밀번호 재확인: 불일치 시 MemberNotFoundException (사용자 식별 방지)
     * - 성공 시 토큰 쿠키 제거
     */
    @Transactional
    public void deleteMyAccount(String authenticatedEmail,
                                DeleteAccountRequest req, // null 가능(정책상 비번 재확인 안하면)
                                HttpServletResponse res) {

        Member m = memberRepository.findByEmail(authenticatedEmail)
                .orElseThrow(MemberNotFoundException::new);

        // (옵션) 비밀번호 재확인 정책 적용 시
        if (req != null && req.getPassword() != null) {
            if (!passwordEncoder.matches(req.getPassword(), m.getPassword())) {
                // 사용자 식별 방지를 위해 동일 예외로 처리
                throw new MemberNotFoundException();
            }
        }

        // 참조 무결성(외래키)이 걸린 도메인이 있다면, JPA 관계/ON DELETE CASCADE/선삭제 처리 필요
        memberRepository.delete(m);

        // 토큰 쿠키 제거
        cookieUtil.clearTokens(res);
    }
}
