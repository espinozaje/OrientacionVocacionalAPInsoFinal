package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    Optional<TestResult> findFirstByUserIdOrderByDateRealizationDesc(Long userId);
    Optional<TestResult> findByUserId(Long id);
}
