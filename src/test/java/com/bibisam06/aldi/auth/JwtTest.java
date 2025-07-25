package com.bibisam06.aldi.auth;

import com.bibisam06.aldi.common.jwt.JwtProperties;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입 로직 테스트")
    public void testSignUp(){
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("password");
        authRequest.setUserEmail("email1");

        User newUser = userService.createUser(authRequest);
        Assertions.assertThat(newUser).isNotNull();

    }


}
