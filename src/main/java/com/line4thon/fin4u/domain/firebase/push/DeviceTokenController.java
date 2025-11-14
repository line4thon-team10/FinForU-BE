package com.line4thon.fin4u.domain.firebase.push;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/push")  // ⭐ 여기서 /api 붙여줌
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenRepository repo;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody DeviceTokenRegisterRequest request) {
        repo.findByToken(request.getToken())
                .forEach(d -> {
                    d.setMemberId(request.getMemberId());
                    d.setActive(true);
                });

        if (repo.findByToken(request.getToken()).isEmpty()) {
            repo.save(DeviceToken.builder()
                    .memberId(request.getMemberId())
                    .token(request.getToken())
                    .active(true)
                    .build());
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deactivate")
    @Transactional
    public ResponseEntity<Void> deactivate(@RequestBody DeviceTokenDeactivateRequest request) {
        repo.findByToken(request.getToken())
                .forEach(d -> d.setActive(false));
        return ResponseEntity.noContent().build();
    }
}