package com.line4thon.fin4u.domain.specialize.web.controller;

import com.line4thon.fin4u.domain.specialize.exception.SpecializeBankNotFoundException;
import com.line4thon.fin4u.global.crawl.Entity.ForeignerStore;
import com.line4thon.fin4u.global.crawl.repository.ForeignerStoreRepository;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/specialize")
@RequiredArgsConstructor
public class SpecializeBankController {
    private final ForeignerStoreRepository repository;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getSpecializeBankInfo() {

        List<ForeignerStore> response = repository.findAll();

        if (response.isEmpty()) {
            throw new SpecializeBankNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }
}
