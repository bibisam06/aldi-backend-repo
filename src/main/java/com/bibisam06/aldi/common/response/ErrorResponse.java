package com.bibisam06.aldi.common.response;

public class ErrorResponse<T> extends BasicResponse {

    private T data;


    public ErrorResponse(int responseCode, String responseMessage,T data) {
        super(responseCode, responseMessage);
        this.data = data;
    }

    // 500 - Internal Server Error
    public static <T> ErrorResponse<T> of(T data){
        return new ErrorResponse<T>(500, "Internal Server Error", data);
    }

    // 404 - not found
    public static <T> ErrorResponse<T> of(){
        return new ErrorResponse<T>(404, "Not Found", null);
    }

    // 404 - not found
    public static <T> ErrorResponse<T> of(int errorCode, String errorMessage){
        return new ErrorResponse<T>(errorCode, errorMessage, null);
    }
}
