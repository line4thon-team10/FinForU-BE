package com.line4thon.fin4u.global.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${security.cookie.access-name}") private String accessName;
    @Value("${security.cookie.refresh-name}") private String refreshName;
    @Value("${security.cookie.domain}") private String domain;
    @Value("${security.cookie.same-site:Lax}") private String sameSite;
    @Value("${security.cookie.secure:false}") private boolean secure;

    // SameSite 속성은 표준 Cookie API로 직접 못걸어서 헤더로 추가
    public void addAccessToken(HttpServletResponse res, String token, int maxAgeSeconds) {
        addCookie(res, accessName, token, maxAgeSeconds);
    }
    public void addRefreshToken(HttpServletResponse res, String token, int maxAgeSeconds) {
        addCookie(res, refreshName, token, maxAgeSeconds);
    }

    public String getAccessToken(HttpServletRequest req) { return getCookie(req, accessName); }
    public String getRefreshToken(HttpServletRequest req) { return getCookie(req, refreshName); }

    public void clearTokens(HttpServletResponse res) {
        addCookie(res, accessName, "", 0);
        addCookie(res, refreshName, "", 0);
    }

    private void addCookie(HttpServletResponse res, String name, String value, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(secure);
//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        //cookie.setDomain(domain);
//        res.addCookie(cookie);

        // SameSite 포함해서 직접 헤더 작성
        String setCookie = "%s=%s; Path=/; Max-Age=%d; HttpOnly%s; SameSite=%s"
                .formatted(
                        name,                // %s
                        value,               // %s
                        maxAge,              // %d
                        secure ? "; Secure" : "", // %s
                        sameSite            // %s
                );

        res.addHeader("Set-Cookie", setCookie);
    }

    private String getCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
