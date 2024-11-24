package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Option;
import com.vocacional.orientacionvocacional.model.entity.Solicitation;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.enums.SolicitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitationRepository extends JpaRepository<Solicitation, Integer> {

        List<Solicitation> findByAdviserId(Integer adviserId);

        List<Solicitation> findByAdviserIdAndStatus(Long adviserId, SolicitationStatus status);
        List<Solicitation> findByStudentIdAndStatus(Long studentId, SolicitationStatus status);

        Optional<Solicitation> findByStudentIdAndAdviserId(Long studentId, Long adviserId);
        Optional<Solicitation> findByStudentAndAdviser(Student student, Adviser adviser);
}
