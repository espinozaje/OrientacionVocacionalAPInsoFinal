package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Advisory;
import com.vocacional.orientacionvocacional.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdvisoryRepository extends JpaRepository<Advisory, Integer> {
    List<Advisory> findByStudentIdOrAdviserId(Integer studentId, Integer adviserId);
    long countByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);
}
