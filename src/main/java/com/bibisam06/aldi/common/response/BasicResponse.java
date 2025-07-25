package com.bibisam06.aldi.common.response;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BasicResponse {

    private final int responseCode;
    private final String responseMessage;

    public BasicResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}