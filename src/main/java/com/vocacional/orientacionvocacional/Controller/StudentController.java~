package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.AdviserDTO;
import com.vocacional.orientacionvocacional.dto.StudentDTO;
import com.vocacional.orientacionvocacional.exception.BadRequestException;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import com.vocacional.orientacionvocacional.service.impl.EmailService;
import com.vocacional.orientacionvocacional.service.impl.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyStudent(@RequestParam String email, @RequestParam String verificationCode) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new BadRequestException("Estudiante no encontrado");
        }

        Student student = optionalStudent.get();
        if (student.isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("message", "La cuenta ya está verificada"));
        }

        if (!student.getVerificationCode().equals(verificationCode)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Código de verificación incorrecto"));
        }

        student.setVerified(true);
        student.setVerificationCode(null);
        studentRepository.save(student);

        return ResponseEntity.ok(Map.of("message", "Cuenta verificada con éxito"));
    }


    @PostMapping("/resend-verification-code")
    public ResponseEntity<Map<String, String>> resendVerificationCode(@RequestParam String email) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(email);

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Estudiante no encontrado"));
        }

        Student student = optionalStudent.get();
        if (student.isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("message", "La cuenta ya está verificada"));
        }


        String newVerificationCode = studentService.generateVerificationCode();
        student.setVerificationCode(newVerificationCode);
        studentRepository.save(student);


        emailService.sendVerificationEmail(student.getEmail(), newVerificationCode);

        return ResponseEntity.ok(Map.of("message", "Nuevo código de verificación enviado"));
    }




    @GetMapping("/profile")
    public ResponseEntity<?> getProfileStudent() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String email = authentication.getName();

        Optional<Student> student = studentService.getStudentProfileByEmail(email);


        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"Perfil del Estudiante no encontrado.\"}");
        }
    }

    @GetMapping("/getStudent/{id}")
    public ResponseEntity<?> adviserById(@PathVariable Long id) {
        Optional<StudentDTO> student = Optional.ofNullable(studentService.findById(id));
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Estudiante no encontrado\"}");
        }
    }
    //6
    @GetMapping("/listStudents")
    public ResponseEntity<List<StudentDTO>> listVerifiedStudents() {
        List<StudentDTO> students = studentService.listVerifiedStudents();
        return ResponseEntity.ok(students);
    }
}