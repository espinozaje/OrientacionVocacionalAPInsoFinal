package com.vocacional.orientacionvocacional.repository;


import com.vocacional.orientacionvocacional.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(Long id);
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);
    List<Student> findByVerifiedTrue();
}
