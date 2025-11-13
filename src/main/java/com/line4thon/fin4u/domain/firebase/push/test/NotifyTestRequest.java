package com.line4thon.fin4u.domain.firebase.push.test;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotifyTestRequest {
    private Long memberId;
    private String type; // INSTALLMENT_DUE_TODAY 이런 문자열
}
