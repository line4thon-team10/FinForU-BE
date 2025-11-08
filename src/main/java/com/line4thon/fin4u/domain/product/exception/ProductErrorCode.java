package com.line4thon.fin4u.domain.product.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements BaseResponseCode {
    NOT_FOUND_CARD_404("NOT_FOUND_CARD_404", 404, "해당 카드 상품을 찾을 수 없습니다."),
    NOT_FOUND_DEPOSIT_404("NOT_FOUND_DEPOSIT_404", 404, "해당 예금 상품을 찾을 수 없습니다."),
    NOT_FOUND_SAVING_404("NOT_FOUND_SAVING_404", 404, "해당 적금 상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_TYPE_400("INVALID_PRODUCT_TYPE_400", 400,"유효하지 않은 상품 타입입니다." );

    private final String code;
    private final int httpStatus;
    private final String message;
}
