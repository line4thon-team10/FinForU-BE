package com.line4thon.fin4u.global.response;

import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
@ToString
public class BaseResponse {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    /* factory method */

    /**
     * @param isSuccess
     * @param baseResponseCode baseResponseCode 객체에서 code와 message를 getter 메소드로 넘겨준다
     */
    public static BaseResponse of(Boolean isSuccess, BaseResponseCode baseResponseCode) {
        return new BaseResponse(isSuccess, baseResponseCode.getCode(), baseResponseCode.getMessage());
    }

    /**
     * @param isSuccess
     * @param baseResponseCode baseResponseCode 객체에서 code를 getter 메소드로 넘겨준다
     * @param message
     */
    public static BaseResponse of(Boolean isSuccess, BaseResponseCode baseResponseCode, String message) {
        return new BaseResponse(isSuccess, baseResponseCode.getCode(), message);
    }

    /**
     * 모든 인자를 직접 넘겨주는 방식
     * @param isSuccess
     * @param code
     * @param message
     */
    public static BaseResponse of(Boolean isSuccess, String code, String message) {
        return new BaseResponse(isSuccess, code, message);
    }

    /**
     * 실패 케이스에 대해서 오직 baseResponseCode만 인자로 받아 BaseResponse 객체를 생성
     * @param baseResponseCode
     */
    public static BaseResponse of(BaseResponseCode baseResponseCode) {
        return new BaseResponse(false, baseResponseCode.getCode(), baseResponseCode.getMessage());
    }
}
