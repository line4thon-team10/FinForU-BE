package com.line4thon.fin4u.domain.preference.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferErrorCode implements BaseResponseCode {
    NOT_FOUND_PREFERENCE_404("NOT_FOUND_PREFERENCE_404", 404,"선호리스트를 찾지 못했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
