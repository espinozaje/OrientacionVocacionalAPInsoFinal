package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.User;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
    List<User> findByRole(ERole role);

}
