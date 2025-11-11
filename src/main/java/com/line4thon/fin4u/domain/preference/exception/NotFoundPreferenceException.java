package com.line4thon.fin4u.domain.preference.exception;

import com.line4thon.fin4u.global.exception.BaseException;

public class NotFoundPreferenceException extends BaseException {
    public NotFoundPreferenceException() {
        super(PreferErrorCode.NOT_FOUND_PREFERENCE_404);
    }
}
