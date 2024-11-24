package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendAdvisorApplication")
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("email") String email) {

        try {
            emailService.sendEmailWithAttachment(nombre, apellido, email, archivo);

            // Respuesta de éxito en formato JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email enviado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Respuesta de error en formato JSON
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al enviar el email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}
