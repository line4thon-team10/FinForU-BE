package com.line4thon.fin4u.domain.recommend.service;

import com.line4thon.fin4u.domain.recommend.web.dto.RecommendRes;

import java.security.Principal;

public interface RecommendService {
    RecommendRes recommend(Principal principal, String langCode);
}
