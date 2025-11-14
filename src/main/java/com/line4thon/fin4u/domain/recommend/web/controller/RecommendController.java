package com.line4thon.fin4u.domain.recommend.web.controller;

import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.domain.recommend.service.RecommendService;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {
    
    private final RecommendService recommendservice;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getRecommend(
            Principal principal,
            Locale locale
    ){
        String langCode = locale.getLanguage();

        ProductFilterRes res = recommendservice.recommend(principal, langCode);

        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }

}
