package com.line4thon.fin4u.domain.specialize.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpecializeBankErrorCode implements BaseResponseCode {

    SPECIALIZE_BANK_NOT_FOUND("SPECIALIZE_BANK_NOT_FOUND_404", 404, "외국인 특화 점포에 대한 정보를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
