package com.line4thon.fin4u.domain.example.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExampleErrorCode implements BaseResponseCode {

    EXAMPLE_NOT_FOUND("EXAMPLE_NOT_FOUND_404", 404, "해당 id를 가진 Example을 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
