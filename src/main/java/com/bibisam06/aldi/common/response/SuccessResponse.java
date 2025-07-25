package com.bibisam06.aldi.common.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SuccessResponse<T> extends BasicResponse {

    private final T data;

    // 생성자 : 부모 생성자 호출 + data 할당
    public SuccessResponse(int responseCode, String responseMessage, T data) {
        super(responseCode, responseMessage);
        this.data = data;
    }

    // 데이터가 있는 성공 응답 생성
    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(200, "OK", data);
    }

    // 빈 성공 응답 (data는 null)
    public static <T> SuccessResponse<T> empty() {
        return new SuccessResponse<>(200, "OK", null);
    }

    // 생성 성공 (201 Created)
    public static <T> SuccessResponse<T> created() {
        return new SuccessResponse<>(201, "Created", null);
    }
}