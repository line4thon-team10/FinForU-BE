package com.line4thon.fin4u.domain.product.web.controller;

import com.line4thon.fin4u.domain.product.service.ProductService;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getFilterProduct(
            @ModelAttribute ProductFilterReq filter
    ) {
        ProductFilterRes res = productService.getFilterProduct(filter);
        return ResponseEntity.status(
                HttpStatus.OK).body(SuccessResponse.ok(res));
    }

    @GetMapping
}
