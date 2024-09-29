package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.UserDTO;
import com.vocacional.orientacionvocacional.model.entity.User;
import com.vocacional.orientacionvocacional.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService usuarioService;


    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Validated @RequestBody UserDTO usuarioDTO) {
        try {
            usuarioService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Error al registrar el usuario.\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        Map<String, String> response = new HashMap<>();
        if (usuarioService.login(email, password)) {
            response.put("message", "Login exitoso");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Credenciales incorrectas");
            return ResponseEntity.status(401).body(response);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            usuarioService.updateUser(id, userDTO);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el usuario: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            usuarioService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }


    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = usuarioService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/obtenertodos")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}/encrypt-password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestParam String newPassword) {
        try {
            usuarioService.updateAndEncryptPassword(id, newPassword);
            return ResponseEntity.ok("Contraseña actualizada y encriptada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            usuarioService.generateResetPasswordToken(email);
            // Devuelve una respuesta con encabezado de tipo JSON
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("message", "Se ha enviado un correo con instrucciones para restablecer su contraseña."));
        } catch (Exception e) {
            // Devuelve una respuesta con encabezado de tipo JSON en caso de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("error", "Error al generar el token de recuperación: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            usuarioService.resetPassword(token, newPassword);
            // Devolver un JSON con un mensaje de éxito
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente."));
        } catch (Exception e) {
            e.printStackTrace(); // Log del error para depuración
            // Devolver un JSON con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al restablecer la contraseña: " + e.getMessage()));
        }
    }
}
