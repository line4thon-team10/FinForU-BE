package com.line4thon.fin4u.domain.member.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 계정 삭제 전 사용자 재확인용(선택).
 * 정책상 비밀번호 확인을 요구하지 않으려면 이 DTO 없이도 구현 가능.
 */
@Getter @Setter
public class DeleteAccountRequest {
    private String password;
}