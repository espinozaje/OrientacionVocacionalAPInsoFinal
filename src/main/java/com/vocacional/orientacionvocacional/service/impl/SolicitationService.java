package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Solicitation;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.enums.SolicitationStatus;
import com.vocacional.orientacionvocacional.repository.AdviserRepository;
import com.vocacional.orientacionvocacional.repository.SolicitationRepository;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitationService {

    private final SolicitationRepository solicitationRepository;
    private final StudentRepository studentRepository;
    private final AdviserRepository adviserRepository;

    public void createSolicitation(Long studentId, Long adviserId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));
        Adviser adviser = adviserRepository.findById(adviserId)
                .orElseThrow(() -> new RuntimeException("Asesor no encontrado."));


        Optional<Solicitation> existingSolicitation = solicitationRepository.findByStudentAndAdviser(student, adviser);

        if (existingSolicitation.isPresent()) {

            Solicitation solicitation = existingSolicitation.get();


            if (solicitation.getStatus() == SolicitationStatus.REJECTED) {
                solicitation.setStatus(SolicitationStatus.PENDING);
                solicitationRepository.save(solicitation);
            }

        } else {

            Solicitation solicitation = new Solicitation();
            solicitation.setStudent(student);
            solicitation.setAdviser(adviser);
            solicitation.setStatus(SolicitationStatus.PENDING);
            solicitationRepository.save(solicitation);
        }
    }

    public List<Solicitation> getSolicitationsByAdviser(Integer adviserId) {
        return solicitationRepository.findByAdviserId(adviserId);
    }

    public void respondToSolicitation(Integer solicitationId, SolicitationStatus status) {
        Solicitation solicitation = solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        solicitation.setStatus(status);
        solicitationRepository.save(solicitation);
    }


    public List<Student> getAcceptedStudentsByAdviser(Long adviserId) {
        return solicitationRepository.findByAdviserIdAndStatus(adviserId, SolicitationStatus.ACCEPTED)
                .stream()
                .map(Solicitation::getStudent)
                .collect(Collectors.toList());
    }

    public List<Adviser> getAcceptedAdvisersByStudent(Long studentId) {
        return solicitationRepository.findByStudentIdAndStatus(studentId, SolicitationStatus.ACCEPTED)
                .stream()
                .map(Solicitation::getAdviser)
                .collect(Collectors.toList());
    }
}
