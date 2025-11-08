package com.line4thon.fin4u.domain.exrate.web.controller;

import com.line4thon.fin4u.domain.exrate.service.ExchangeRateService;
import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exrate")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getExchangeRate() {
        List<ExchangeRateRes> response = exchangeRateService.getExchangeRateGraphData();
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }


}
