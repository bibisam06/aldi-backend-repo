package com.bibisam06.aldi.auth.service;

import com.bibisam06.aldi.CustomUserDetails;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일 기준으로 조회한다고 가정
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new CustomUserDetails(user);
    }

    // custom method
    public CustomUserDetails loadUserByUserId(Integer userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new CustomUserDetails(user);
    }
}
