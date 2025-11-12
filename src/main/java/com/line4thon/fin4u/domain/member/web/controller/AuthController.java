package com.line4thon.fin4u.domain.member.web.controller;

import com.line4thon.fin4u.domain.member.exception.MemberNotFoundException;
import com.line4thon.fin4u.domain.member.web.dto.request.LoginRequest;
import com.line4thon.fin4u.domain.member.web.dto.request.SignupRequest;
import com.line4thon.fin4u.domain.member.web.dto.request.UpdateMemberRequest;
import com.line4thon.fin4u.domain.member.web.dto.response.AuthResponse;
import com.line4thon.fin4u.domain.member.web.dto.response.MemberResponse;
import com.line4thon.fin4u.domain.member.web.dto.request.DeleteAccountRequest;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.member.service.AuthService;
import com.line4thon.fin4u.global.security.JwtProvider;
import com.line4thon.fin4u.global.web.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private String currentUserEmail() {
        var ctx = SecurityContextHolder.getContext();
        var auth = (ctx != null) ? ctx.getAuthentication() : null;
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();

        // 1) UserDetails 계열이면 username 반환 (보통 이메일을 username으로 씀)
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            return ud.getUsername();
        }
        // 2) 문자열이면 그대로 사용 (단, anonymousUser는 제외)
        if (principal instanceof String s) {
            return "anonymousUser".equals(s) ? null : s;
        }
        // 3) 마지막 보루: getName()에 subject가 들어오는 경우가 많음
        String name = auth.getName();
        return "anonymousUser".equals(name) ? null : name;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request,
            HttpServletResponse res
    ) {
        AuthResponse response = authService.signup(request, res);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse res
    ) {
        AuthResponse response = authService.login(request, res);
        return ResponseEntity.ok(response);
    }

    /** 로그아웃: 토큰 쿠키 제거 */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        authService.logout(res);
        return ResponseEntity.noContent().build();
    }

    // 토큰 재발급용으로 만들어 두긴 헀으나 엄청 필요할 것 같지 않아 일단 명세서에 써놓지 않음
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest req, HttpServletResponse res) {
        String refresh = cookieUtil.getRefreshToken(req);
        if (!StringUtils.hasText(refresh) || !jwtProvider.isValid(refresh)) {
            return ResponseEntity.status(401).build();
        }
        authService.reissueAccess(refresh, res);
        return ResponseEntity.noContent().build();
    }

    // 내 정보 조회 (JWT 인증 기반)
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me() {
        String email = currentUserEmail();
        if (!StringUtils.hasText(email)) return ResponseEntity.status(401).build();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException());
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    //내 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> editMe(
            @RequestBody UpdateMemberRequest req
    ) {
        String email = currentUserEmail();
        if (!StringUtils.hasText(email)) return ResponseEntity.status(401).build();

        Member m = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (req.getNationality() != null) m.setNationality(req.getNationality());
        if (req.getLanguage() != null) m.setLanguage(req.getLanguage());
        if (req.getVisaType() != null) m.setVisaType(req.getVisaType());
        if (req.getVisaExpir() != null) m.setVisa_expir(req.getVisaExpir());
        if (req.getNotify() != null) m.setNotify(req.getNotify());
        if (req.getDesiredProductType() != null) m.setDesiredProductType(req.getDesiredProductType());

        // JPA dirty checking으로 업데이트
        return ResponseEntity.ok(MemberResponse.from(m));
    }

    /**
     * 회원 탈퇴(계정 삭제)
     * - 인증 필요(JWT)
     * - (옵션) 비밀번호 재확인을 요구한다면 body에 password 포함
     * - 성공 시 204 + 쿠키 제거
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(
            @RequestBody(required = false) DeleteAccountRequest request,
            HttpServletResponse res
    ) {
        String email = currentUserEmail();
        if (!StringUtils.hasText(email)) return ResponseEntity.status(401).build();

        authService.deleteMyAccount(email, request, res);
        return ResponseEntity.noContent().build();
    }
}

