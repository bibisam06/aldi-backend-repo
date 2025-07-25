package com.bibisam06.aldi.common.jwt.dto;

import lombok.Getter;

@Getter
public class AccessTokenDTO {

    private final String userId;
    private final String userRole;

    public AccessTokenDTO(String subject, String role) {
        this.userId = subject;
        this.userRole = role;
    }
}
