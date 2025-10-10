package com.line4thon.fin4u.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.line4thon.fin4u.global.response.code.BaseResponseCode;
import com.line4thon.fin4u.global.response.code.SuccessResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"isSuccess", "timeStamp", "code", "httpStatus", "message", "data"})
public class SuccessResponse<T> extends BaseResponse {
    private final int httpStatus;
    private final T data;

    @Builder
    public SuccessResponse(T data, BaseResponseCode baseResponseCode) {
        super(true, baseResponseCode.getCode(), baseResponseCode.getMessage());
        this.httpStatus = baseResponseCode.getHttpStatus();
        this.data = data;
    }

    /* factory method */

    // 200 OK
    public static <T> SuccessResponse<T> ok(T data) {
        return new SuccessResponse<>(data, SuccessResponseCode.SUCCESS_OK);
    }

    // 201 CREATED
    public static <T> SuccessResponse<T> created(T data) {
        return new SuccessResponse<>(data, SuccessResponseCode.SUCCESS_CREATED);
    }

    /**
     * data == null 성공 응답 생성자
     * @param <T>
     */
    public static <T> SuccessResponse<T> empty() {
        return new SuccessResponse<>(null, SuccessResponseCode.SUCCESS_OK);
    }

    /**
     * for Custom ResponseCode with data
     * @param data
     * @param baseResponseCode
     * @param <T>
     */
    public static <T> SuccessResponse<T> of(T data, BaseResponseCode baseResponseCode) {
        return new SuccessResponse<>(data, baseResponseCode);
    }

    /**
     * for Custom ResponseCode without data
     * @param baseResponseCode
     * @return
     */
    public static SuccessResponse<?> from(BaseResponseCode baseResponseCode) {
        return new SuccessResponse<>(null, baseResponseCode);
    }
}
