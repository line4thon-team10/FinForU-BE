package com.line4thon.fin4u.domain.consent.service;

import com.line4thon.fin4u.domain.consent.exception.EssentialConsentException;
import com.line4thon.fin4u.domain.consent.web.dto.ConsentRequest;
import org.springframework.stereotype.Service;

@Service
public class ConsentService {

    public void validateConsent(ConsentRequest req) {
        if (!Boolean.TRUE.equals(req.getAgreeFinforu()) ||
                !Boolean.TRUE.equals(req.getAgreeTerms14()) ||
                !Boolean.TRUE.equals(req.getAgreePrivacy())) {
            throw new EssentialConsentException();
        }

        // ✅ 실제 DB 저장은 하지 않고 로깅만 (필요하면 콘솔로 출력)
        System.out.println("[CONSENT] 필수 약관 동의 완료");
        System.out.println(" - FinForU: " + req.getAgreeFinforu());
        System.out.println(" - Terms14: " + req.getAgreeTerms14());
        System.out.println(" - Privacy: " + req.getAgreePrivacy());
        System.out.println(" - Promotion: " + req.getAgreePromotion());
        System.out.println(" - Marketing: " + req.getAgreeMarketing());
    }
}
