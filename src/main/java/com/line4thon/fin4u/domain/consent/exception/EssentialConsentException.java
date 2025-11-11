package com.line4thon.fin4u.domain.consent.exception;

import com.line4thon.fin4u.domain.consent.exception.ConsentErrorCode;
import com.line4thon.fin4u.global.exception.BaseException;

public class EssentialConsentException extends BaseException {
    public EssentialConsentException(){
        super(ConsentErrorCode.ESSENTIAL_CONSENT);

    }
}
