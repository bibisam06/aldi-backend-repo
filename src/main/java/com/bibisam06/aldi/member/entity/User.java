package com.bibisam06.aldi.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String userEmail;

    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

}
