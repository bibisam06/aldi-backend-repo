package com.bibisam06.aldi.member.repository;

import com.bibisam06.aldi.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User save(User user);

    User findByUserEmail(String email);
}
