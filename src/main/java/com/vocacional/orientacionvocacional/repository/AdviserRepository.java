package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Adviser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdviserRepository extends JpaRepository<Adviser, Long> {
    Optional<Adviser> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Adviser> findByEmail(String email);
}