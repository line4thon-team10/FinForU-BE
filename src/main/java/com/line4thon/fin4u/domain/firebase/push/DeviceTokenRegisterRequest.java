package com.line4thon.fin4u.domain.firebase.push;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeviceTokenRegisterRequest {
    private Long memberId;
    private String token;
}
