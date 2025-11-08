package com.line4thon.fin4u.domain.member.exception;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//오류 메시지를 한 곳에서 관리
@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseResponseCode {
    DUPLICATE_EMAIL(409, "MEMBER-001", "이미 사용 중인 이메일입니다."),
    MEMBER_NOT_FOUND(404, "MEMBER-002", "회원 정보를 찾을 수 없습니다.");
    private final int httpStatus;
    private final String code;
    private final String message;
}
