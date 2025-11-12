package com.line4thon.fin4u.domain.product.web.controller;

import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.service.Product.ProductServiceImpl;
import com.line4thon.fin4u.domain.product.web.dto.ProductDetailRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    // 상품 검색(필터링)
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getFilterProduct(
            @ModelAttribute ProductFilterReq filter,
            Locale locale
    ) {
        String langCode = locale.getLanguage();

        ProductFilterRes res = productService.getFilterProduct(filter, langCode);
        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }

    // 상품 상세 조회
    @GetMapping("/{type}/{id}")
    public ResponseEntity<SuccessResponse<?>> getProductDetail(
            @PathVariable(value = "type", required = true) Type type,
            @PathVariable(value = "id", required = true) Long id,
            Locale locale
    ){
        String langCode = locale.getLanguage();

        ProductDetailRes res = productService.getProductDetail(type, id, langCode);
        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }
}
