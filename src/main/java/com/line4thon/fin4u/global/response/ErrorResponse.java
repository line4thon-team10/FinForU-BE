package com.line4thon.fin4u.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"isSuccess", "timeStamp", "code", "httpStatus", "message", "data"})
public class ErrorResponse<T> extends BaseResponse {

    private final int httpStatus;
    private final T data;
    /**
     * @param data 제네릭으로 받는 변수 data, 응답에 포함된다.
     * @param baseResponseCode baseResponseCode 객체를 사용해 message, httpStatus를 완성한다.
     */
    @Builder
    public ErrorResponse(T data, BaseResponseCode baseResponseCode) {
        super(false, baseResponseCode.getCode(), baseResponseCode.getMessage());
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }


    /**
     * @param data 제네릭으로 받는 변수 data, 응답에 포함된다.
     * @param baseResponseCode baseResponseCode 객체를 사용해 httpStatus를 완성한다.
     * @param message CustomMessage를 작성할 수 있다.
     */
    @Builder
    public ErrorResponse(T data, BaseResponseCode baseResponseCode, String message) {
        super(false, baseResponseCode.getCode(), message);
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    /* factory method */

    /**
     * data 없는 ErrorResponse 객체를 생성
     * @param baseResponseCode baseResponseCode 객체만 인자로 받는다
     */
    public static ErrorResponse<?> from(BaseResponseCode baseResponseCode) {
        return new ErrorResponse<> (null, baseResponseCode);
    }

    /**
     * data가 없고, message를 custom으로 생성
     * @param baseResponseCode httpStatus를 getter 메소드로 넘기기 위함이다
     * @param message CustomMessage를 인자로 받는다
     */
    public static ErrorResponse<?> of(BaseResponseCode baseResponseCode, String message) {
        return new ErrorResponse<> (null, baseResponseCode, message);
    }

    /**
     * data가 있는 ErrorResponse 생성자
     * @param baseResponseCode httpStatus, message, code를 BaseResponseCode 객체에서 가져온다
     * @param data 제네릭 변수 data이다.
     * @param <T>
     */

    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data) {
        return new ErrorResponse<>(data, baseResponseCode);
    }

    /**
     * data가 있는 ErrorResponse 생성자
     * @param baseResponseCode httpStatus, code를 baseResponseCode 객체에서 가져온다
     * @param data 제네릭 변수 data이다
     * @param message CustomMessage를 작성할 수 있다
     * @param <T>
     */
    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data, String message) {
        return new ErrorResponse<>(data, baseResponseCode, message);
    }

}
