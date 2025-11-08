package com.line4thon.fin4u.domain.member.exception;

import com.line4thon.fin4u.global.exception.BaseException;

//해당 회원을 찾을 수 없을 때 발생하는 예외
public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException(){
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}


