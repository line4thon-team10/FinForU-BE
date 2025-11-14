package com.line4thon.fin4u.domain.recommend.fastApi.controller;

import com.line4thon.fin4u.domain.recommend.fastApi.service.UpsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upsert_all")
public class UpsertController {
    private final UpsertService upsertService;

    // 전체 파일 전송
    @PostMapping
    public String upsertAll() {
        upsertService.upsertAllProducts();
        return "모든 금융 상품 파이썬으로 전송됨";
    }
}
