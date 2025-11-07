package com.line4thon.fin4u.domain.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private MemberResponse member;
    private String message;
}