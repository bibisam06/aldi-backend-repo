package com.bibisam06.aldi;

import com.bibisam06.aldi.member.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;


@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // enum 권한 처리 방식
        return Collections.singleton(() -> user.getUserRole().name());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요 시 계정 만료 로직 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요 시 잠금 여부 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용 여부
    }
}
