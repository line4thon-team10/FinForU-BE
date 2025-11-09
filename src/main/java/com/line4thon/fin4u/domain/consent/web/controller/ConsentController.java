package com.line4thon.fin4u.domain.consent.web.controller;

import com.line4thon.fin4u.domain.consent.web.dto.ConsentRequest;
import com.line4thon.fin4u.domain.consent.service.ConsentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping
    public ResponseEntity<Void> submit(@Valid @RequestBody ConsentRequest req) {
        consentService.validateConsent(req);
        return ResponseEntity.noContent().build(); // 204 성공
    }
}