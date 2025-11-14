package com.line4thon.fin4u.domain.product.web.controller;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.service.Comparison.ComparisonService;
import com.line4thon.fin4u.domain.product.service.Comparison.ComparisonServiceImpl;
import com.line4thon.fin4u.domain.product.web.dto.CompareRes;
import com.line4thon.fin4u.domain.product.web.dto.CompareSaveReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/products/comparison")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    //바구니에 저장
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> saveProducts(
            Principal principal,
            @RequestParam(required = false) String guestToken,
            @Valid @RequestBody CompareSaveReq req
            ){
        comparisonService.saveProduct(principal, guestToken, req.type(), req.productId());

        return ResponseEntity.status(
                HttpStatus.CREATED).body(SuccessResponse.created("저장완료"));
    }

    //바구니 리스트 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getProductsList(
            Principal principal,
            @RequestParam(required = false) String guestToken,
            @Valid @ModelAttribute ProductFilterReq req,
            Locale locale
    ){
        String langCode = locale.getLanguage();
        ProductFilterRes res = comparisonService.getComparisonFilter(principal, guestToken, req, langCode);

        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }

    //상품비교
    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<?>> comparing(
            @RequestParam(value = "type", required = true) Type type,
            @RequestParam(value = "productIds", required = true)List<Long> productIds,
            Locale locale
    ){

        String langCode = locale.getLanguage();

        CompareRes res = comparisonService.compare(productIds, type, langCode);
        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }
}
