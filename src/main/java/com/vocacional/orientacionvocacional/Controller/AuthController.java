package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.*;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.entity.User;
import com.vocacional.orientacionvocacional.repository.AdviserRepository;
import com.vocacional.orientacionvocacional.repository.StudentRepository;
import com.vocacional.orientacionvocacional.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdviserService adviserService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdviserRepository adviserRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private HttpServletRequest request;



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Error al registrar el usuario: " + e.getMessage() + "\"}");
        }
    }


    @PostMapping("/registerStudent")
    public void registerStudent(@Validated @RequestBody StudentDTO studentDTO) {
            studentService.registerStudent(studentDTO);
            ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
    }


    @PostMapping("/registerAdviser")
    public void registerAdvisor(@Validated @RequestBody AdviserDTO adviserDTO) {
            adviserService.registerAdvisor(adviserDTO);
          ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();

        Optional<Student> optionalStudent = studentRepository.findByEmail(email);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            if (!student.isVerified()) {
                response.put("message", "Por favor, verifica tu correo electrónico antes de iniciar sesión");
                return ResponseEntity.badRequest().body(response);
            }

            if (!passwordEncoder.matches(password, student.getPassword())) {
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }

            String token = jwtUtilService.generateToken(student);
            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);
            return ResponseEntity.ok(response);
        }

        Optional<Adviser> optionalAdvisor = adviserRepository.findByEmail(email);
        if (optionalAdvisor.isPresent()) {
            Adviser advisor = optionalAdvisor.get();

            if (!passwordEncoder.matches(password, advisor.getPassword())) {
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }

            // Aquí cambias la lógica para enviar siempre el token, incluso si el cambio de contraseña es necesario
            String token = jwtUtilService.generateToken(advisor);

            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);

            // Agregamos "requiresPasswordChange" para indicarlo al frontend
            response.put("requiresPasswordChange", advisor.isNeedsPasswordChange());

            return ResponseEntity.ok(response);
        }

        response.put("message", "Correo electrónico no registrado");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestParam String email, @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Optional<Adviser> optionalAdvisor = adviserRepository.findByEmail(email);
        if (optionalAdvisor.isPresent()) {
            Adviser advisor = optionalAdvisor.get();

            // Actualiza la contraseña
            advisor.setPassword(passwordEncoder.encode(newPassword));
            advisor.setNeedsPasswordChange(false);  // El asesor ya no necesita cambiar la contraseña
            adviserRepository.save(advisor);  // Guarda el asesor actualizado

            response.put("message", "Contraseña cambiada exitosamente");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Asesor no encontrado");
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserProfile(@RequestBody Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(Long.valueOf(updatedStudent.getId()));

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Estudiante no encontrado"));
        }

        Student student = optionalStudent.get();


        Optional<Student> existingStudent = studentRepository.findByFirstNameAndLastName(updatedStudent.getFirstName(), updatedStudent.getLastName());
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(student.getId())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ya existe un estudiante con el mismo nombre y apellido"));
        }


        // Si no hay cambios en el correo, se puede actualizar el perfil
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setImg_profile(updatedStudent.getImg_profile());

        studentRepository.save(student);
        return ResponseEntity.ok(Map.of("message", "Perfil actualizado exitosamente."));
    }


    @PutMapping("/update-image")
    public ResponseEntity<FileResponseDTO> updateProfileImage(@RequestParam("file") MultipartFile file,
                                                              @RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        Integer userId = jwtUtilService.extractUserId(jwt);


        String newFilePath = fileStorageService.store(file);


        userService.updateProfileImage(userId, newFilePath);


        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder.fromHttpUrl(host)
                .path("/api/v1/auth/")
                .path(newFilePath)
                .toUriString();

        FileResponseDTO response = new FileResponseDTO(url);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
       String path = fileStorageService.store(file);
       String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
       String url = ServletUriComponentsBuilder
               .fromHttpUrl(host)
               .path("/api/v1/auth/")
               .path(path)
               .toUriString();

       return Map.of("url", url);
    }


    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = fileStorageService.loadResource(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }


    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el usuario: " + e.getMessage());
        }
    }




    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}/encrypt-password")
    public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String newPassword) {
        try {
            userService.updateAndEncryptPassword(id, newPassword);
            return ResponseEntity.ok("Contraseña actualizada y encriptada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            userService.generateResetPasswordToken(email);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("message", "Se ha enviado un correo con instrucciones para restablecer su contraseña."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("error", "Error al generar el token de recuperación: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);

            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al restablecer la contraseña: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        String email = jwtUtilService.extractUsername(jwt);
        User user = userService.findByEmail(email);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
