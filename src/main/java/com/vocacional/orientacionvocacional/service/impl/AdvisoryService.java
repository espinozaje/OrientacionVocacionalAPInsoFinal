package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.Mapper.AdvisoryMapper;
import com.vocacional.orientacionvocacional.dto.AdvisoryDTO;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Advisory;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.repository.AdviserRepository;
import com.vocacional.orientacionvocacional.repository.AdvisoryRepository;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdvisoryService {

    @Autowired
    private AdvisoryRepository advisoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdviserRepository adviserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdvisoryMapper advisoryMapper;

    @Transactional
    public Advisory createAdvisory(AdvisoryDTO advisoryDTO) {
        Integer randomId = generateUniqueRandomId();
        Student student = studentRepository.findById(Long.valueOf(advisoryDTO.getStudentId()))
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Adviser adviser = adviserRepository.findById(Long.valueOf(advisoryDTO.getAdviserId()))
                .orElseThrow(() -> new RuntimeException("Asesor no encontrado"));

        // Verificar el número de asesorías del estudiante en el mes actual
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        long advisoryCount = advisoryRepository.countByStudentAndDateBetween(
                student,
                LocalDate.of(currentYear, currentMonth, 1),  // Inicio del mes actual
                LocalDate.of(currentYear, currentMonth, 30)  // Fin del mes actual
        );

        // Si el estudiante ya tiene 2 asesorías este mes, lanzar un error
        if (advisoryCount >= 2) {
            throw new RuntimeException("El estudiante ya tiene 2 asesorías programadas este mes.");
        }

        Advisory advisory = new Advisory();
        advisory.setId(randomId);
        advisory.setLink(advisoryDTO.getLink());
        advisory.setName(advisoryDTO.getName());
        advisory.setDate(advisoryDTO.getDate());
        advisory.setTime(advisoryDTO.getTime());
        advisory.setStudent(student);
        advisory.setAdviser(adviser);

        advisoryRepository.save(advisory);

        String subject = "Nueva Asesoría Programada";
        String body = emailService.generateHtmlBodycreateAdvisory(student, advisory, adviser);
        emailService.sendHtmlEmail(student.getEmail(), subject, body);

        return advisory;
    }

    private Integer generateUniqueRandomId() {
        Random random = new Random();
        Integer randomId;

        do {
            randomId = 1000000 + random.nextInt(9000000);
        } while (advisoryRepository.existsById(randomId));

        return randomId;
    }


    @Transactional
    public Advisory updateAdvisory(Integer id, AdvisoryDTO advisoryDTO) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found"));


        String previousLink = advisory.getLink();
        String previousName = advisory.getName();
        LocalDate previousDate = advisory.getDate();
        LocalTime previousTime = advisory.getTime();


        advisory.setLink(advisoryDTO.getLink());
        advisory.setName(advisoryDTO.getName());
        advisory.setDate(advisoryDTO.getDate());
        advisory.setTime(advisoryDTO.getTime());


        Advisory updatedAdvisory = advisoryRepository.save(advisory);


        emailService.sendReprogrammingEmail(advisory.getStudent().getEmail(), previousLink, previousName, previousDate, previousTime, advisory);

        return updatedAdvisory;
    }


    @Transactional
    public void deleteAdvisory(Integer id) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found"));


        String studentEmail = advisory.getStudent().getEmail();
        String advisoryName = advisory.getName();
        String advisoryLink = advisory.getLink();
        LocalDate advisoryDate = advisory.getDate();
        LocalTime advisoryTime = advisory.getTime();


        advisoryRepository.deleteById(id);


        emailService.sendCancellationEmail(studentEmail, advisoryName, advisoryLink, advisoryDate, advisoryTime);
    }


    public List<AdvisoryDTO> getAdvisoriesByUserId(Integer userId) {
        List<Advisory> advisories = advisoryRepository.findByStudentIdOrAdviserId(userId, userId);
        return advisories.stream()
                .map(advisoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
