package com.bibisam06.aldi.member.dto;

import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.entity.UserRole;
import com.bibisam06.aldi.member.repository.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@NoArgsConstructor
@Getter
public class AuthRequest {

    private String userEmail;
    private String password;


}
