package com.bibisam06.aldi.member.service;

import com.bibisam06.aldi.common.jwt.JwtProperties;
import com.bibisam06.aldi.common.jwt.JwtProvider;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.entity.UserRole;
import com.bibisam06.aldi.member.repository.UserRepository;
import com.fasterxml.jackson.core.Base64Variant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // 로직 - 회원가입
    public JwtToken createUser(AuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.builder()
                .userEmail(request.getUserEmail())
                .userPassword(encodedPassword)
                .userRole(UserRole.USER)
                .build();

        userRepository.save(newUser);
        return jwtProvider.generateToken(newUser.getUserId(), newUser.getUserRole());
    }

    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

}
