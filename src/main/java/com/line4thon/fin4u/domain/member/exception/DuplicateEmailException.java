package com.line4thon.fin4u.domain.member.exception;

//중복된 이메일로 회원가입하려 할 때 발생하는 예외
import com.line4thon.fin4u.global.exception.BaseException;

public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException(){
        super(MemberErrorCode.DUPLICATE_EMAIL);
    }
}