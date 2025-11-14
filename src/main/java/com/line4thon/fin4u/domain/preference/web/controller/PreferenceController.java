package com.line4thon.fin4u.domain.preference.web.controller;

import com.line4thon.fin4u.domain.preference.service.PreferService;
import com.line4thon.fin4u.domain.preference.service.PreferServiceImpl;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferReq;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/preference")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferService preferService;

    // 저장&수정
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> savePrefer(
            Principal principal,
            @Valid @RequestBody SavePreferReq req
    ){
        SavePreferRes res = preferService.savePrefer(principal, req);

        return ResponseEntity.status(
                HttpStatus.CREATED).body(SuccessResponse.created(res));
    }

    // 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getPrefer(
            Principal principal
    ){
        SavePreferRes res = preferService.getPrefer(principal);

        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }

}
