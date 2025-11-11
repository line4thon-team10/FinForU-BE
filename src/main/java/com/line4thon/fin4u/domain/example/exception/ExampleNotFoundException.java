package com.line4thon.fin4u.domain.example.exception;

import com.line4thon.fin4u.global.exception.BaseException;

import static com.line4thon.fin4u.domain.example.exception.ExampleErrorCode.EXAMPLE_NOT_FOUND;

public class ExampleNotFoundException extends BaseException {
    public ExampleNotFoundException() {
        super(EXAMPLE_NOT_FOUND);
    }
}
