package com.line4thon.fin4u.domain.product.web.controller;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.service.Comparison.ComparisonServiceImpl;
import com.line4thon.fin4u.domain.product.web.dto.ComparisonSaveReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/products/comparison")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonServiceImpl comparisonService;

    //바구니에 저장
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> saveProducts(
            Principal principal,
            @RequestParam(required = false) String guestToken,
            @RequestBody ComparisonSaveReq req
            ){
        String email = (principal != null) ? principal.getName() : null;
        comparisonService.saveProduct(email, guestToken, req.type(), req.productId());

        return ResponseEntity.status(
                HttpStatus.CREATED).body(SuccessResponse.created("저장완료"));
    }

    //바구니 리스트 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getProductsList(
            Principal principal,
            @RequestParam(required = false) String guestToken,
            @Valid @ModelAttribute ProductFilterReq req
    ){
        String email = (principal != null) ? principal.getName() : null;

        ProductFilterRes res = comparisonService.getComparisonFilter(email, guestToken, req);

        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }
}
