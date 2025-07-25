package com.bibisam06.aldi.member.controller;

import com.bibisam06.aldi.common.response.SuccessResponse;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "AUTH", description = "AUTH 관련 API 입니다")
public class UserController {

    private final UserService userService;


    @Operation(summary = "사용자 로그인 API 입니다 - 이메일 로그인")
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @Operation(summary = "사용자 회원가입 API 입니다 - 이메일 회원가입")
    @PostMapping("/signup")
    public SuccessResponse<Object> signup(@RequestBody AuthRequest authRequest) {
        userService.createUser(authRequest);
        return new SuccessResponse<>(200, "회원가입에 성공했습니다", null);
    }
}
