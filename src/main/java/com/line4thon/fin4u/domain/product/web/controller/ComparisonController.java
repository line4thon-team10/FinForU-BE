package com.line4thon.fin4u.domain.product.web.controller;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.service.Comparison.ComparisonServiceImpl;
import com.line4thon.fin4u.domain.product.web.dto.ComparisonSaveReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/comparison")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonServiceImpl comparisonService;

    //바구니에 저장
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> saveProducts(
            @AuthenticationPrincipal Member member,
            @RequestBody ComparisonSaveReq req
            ){
        comparisonService.saveProduct(member, req.guestToken(), req.type(), req.productId());

        return ResponseEntity.status(
                HttpStatus.CREATED).body(SuccessResponse.created("저장완료"));
    }

    //바구니 리스트 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getProductsList(
            @AuthenticationPrincipal Member member,
            @RequestParam(required = false) String guestToken,
            @Valid @ModelAttribute ProductFilterReq req
    ){
        ProductFilterRes res = comparisonService.getComparisonFilter(member, guestToken, req);

        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }
}
