package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.Mapper.AdviserMapper;
import com.vocacional.orientacionvocacional.Mapper.StudentMapper;
import com.vocacional.orientacionvocacional.dto.StudentDTO;
import com.vocacional.orientacionvocacional.exception.BadRequestException;
import com.vocacional.orientacionvocacional.exception.ResourceNotFoundException;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import com.vocacional.orientacionvocacional.model.enums.Plan;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StudentService {
    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EmailService emailService;

    public List<StudentDTO> listVerifiedStudents() {
        List<Student> students = studentRepository.findByVerifiedTrue();
        return students.stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public Optional<Student> getStudentProfileByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public StudentDTO findById(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("El estudiante con ID " + id+ "no fue encontrado"));
        return studentMapper.toDTO(student);
    }

    public StudentDTO registerStudent(StudentDTO studentDTO) {
        Integer randomId = generateUniqueRandomId();

        String verificationCode = generateVerificationCode();

        Student estudiante = studentMapper.toEntity(studentDTO);
        ERole eRole = ERole.STUDENT;
        Plan plan = Plan.FREE;
        estudiante.setId(randomId);
        estudiante.setImg_profile("profile.png");
        estudiante.setFirstName(studentDTO.getFirstName());
        estudiante.setLastName(studentDTO.getLastName());
        estudiante.setEmail(studentDTO.getEmail());
        estudiante.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        estudiante.setRole(eRole);
        estudiante.setPlan(plan);
        estudiante.setVerificationCode(verificationCode);
        estudiante.setVerified(false);

        estudiante = usuarioRepository.save(estudiante);


        emailService.sendVerificationEmail(estudiante.getEmail(), verificationCode);

        return studentMapper.toDTO(estudiante);
    }


    public String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Genera un código de 6 dígitos
    }

    private Integer generateUniqueRandomId() {
        Random random = new Random();
        Integer randomId;

        do {
            randomId = 1000000 + random.nextInt(9000000);
        } while (studentRepository.existsById(Long.valueOf(randomId)));

        return randomId;
    }
}
