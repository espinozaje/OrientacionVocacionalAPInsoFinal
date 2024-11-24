package com.vocacional.orientacionvocacional.service.impl;


import com.vocacional.orientacionvocacional.dto.UserDTO;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.entity.User;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import com.vocacional.orientacionvocacional.model.enums.Plan;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private FileStorageService fileStorageService; // Para almacenar los archivos
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JwtUtilService jwtUtilService;


    public String updateStudentPlanToPremium(Integer userId) {
        Optional<Student> studentOptional = studentRepository.findById(Long.valueOf(userId));
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (student.getPlan() != Plan.PREMIUM) {
                student.setPlan(Plan.PREMIUM);
                studentRepository.save(student);

                // Después de actualizar el plan, generamos un nuevo token
                return jwtUtilService.generateToken(student);  // Genera un token con la nueva información
            }
        } else {
            throw new IllegalArgumentException("No se encontró un estudiante con el ID: " + userId);
        }
        return null; // Retornamos null si no hubo cambio en el plan
    }

    public User findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateProfileImage(Integer userId, String newFilePath) {
        User user = findById(userId);
        user.setImg_profile(newFilePath);
        userRepository.save(user);
    }

    public void registerUser(UserDTO userDTO) {
        User user;
        if (userDTO.getRole() == ERole.STUDENT) {
            user = new Student();
        } else if (userDTO.getRole() == ERole.ADVISER) {
            user = new Adviser();
        } else {
            throw new IllegalArgumentException("Rol no válido");
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }




    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    public void deleteUser(Integer id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));

        userRepository.delete(user);
    }


    public User getUserById(Integer id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void updateAndEncryptPassword(Integer id, String newPassword) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));


        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);


        userRepository.save(user);
    }


    public void generateResetPasswordToken(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("No se encontró un usuario con ese correo.");
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);


        sendResetPasswordEmail(user.getEmail(), token);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) throws Exception {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new Exception("Token inválido.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private void sendResetPasswordEmail(String email, String token) throws Exception {
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email);
        helper.setSubject("Restablecer tu contraseña en OrientacionVocacional");
        helper.setText("<p>Hola,</p>" +
                "<p>Esperamos que estés teniendo un buen día. Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en Orientacion Vocacional, que está diseñada para ayudarte en tu camino de orientación vocacional.</p>" +
                "<p>Si deseas restablecer tu contraseña, haz clic en el enlace de abajo:</p>" +
                "<a href=\"" + resetLink + "\">Restablecer contraseña</a>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
                "<p>¡Gracias por formar parte de nuestra comunidad!</p>" +
                "<p>Atentamente,<br>El equipo de desarrollo de Orientacion Vocacional</p>", true);

        mailSender.send(message);
    }
}
