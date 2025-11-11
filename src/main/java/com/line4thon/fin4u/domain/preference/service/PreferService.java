package com.line4thon.fin4u.domain.preference.service;

import com.line4thon.fin4u.domain.preference.web.dto.SavePreferReq;
import com.line4thon.fin4u.domain.preference.web.dto.SavePreferRes;
import jakarta.validation.Valid;

import java.security.Principal;

public interface PreferService {
    SavePreferRes getPrefer(Principal principal);

    SavePreferRes savePrefer(Principal principal, @Valid SavePreferReq req);

}
