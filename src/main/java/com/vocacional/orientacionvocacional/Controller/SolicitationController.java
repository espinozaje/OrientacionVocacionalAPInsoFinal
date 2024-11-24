package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Solicitation;
import com.vocacional.orientacionvocacional.model.entity.Student;
import com.vocacional.orientacionvocacional.model.enums.SolicitationStatus;
import com.vocacional.orientacionvocacional.repository.SolicitationRepository;
import com.vocacional.orientacionvocacional.service.impl.SolicitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/solicitations")
@RequiredArgsConstructor
public class SolicitationController {

    private final SolicitationService solicitationService;
    private final SolicitationRepository solicitationRepository;


    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendSolicitation(@RequestParam Long studentId, @RequestParam Long adviserId) {
        solicitationService.createSolicitation(studentId, adviserId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud enviada con éxito.");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/adviser/{adviserId}")
    public ResponseEntity<List<Solicitation>> getSolicitationsForAdviser(@PathVariable Integer adviserId) {
        return ResponseEntity.ok(solicitationService.getSolicitationsByAdviser(adviserId));
    }


    @PostMapping("/{solicitationId}/respond")
    public ResponseEntity<Map<String, String>> respondToSolicitation(
            @PathVariable Integer solicitationId,
            @RequestParam SolicitationStatus status) {
        solicitationService.respondToSolicitation(solicitationId, status);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud actualizada.");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/accepted-students")
    public ResponseEntity<List<Student>> getAcceptedStudents(@RequestParam Long adviserId) {
        List<Student> acceptedStudents = solicitationService.getAcceptedStudentsByAdviser(adviserId);
        return ResponseEntity.ok(acceptedStudents);
    }

    @GetMapping("/solicitation-status")
    public ResponseEntity<Map<String, String>> getSolicitationStatus(@RequestParam Long studentId, @RequestParam Long adviserId) {
        Optional<Solicitation> solicitation = solicitationRepository.findByStudentIdAndAdviserId(studentId, adviserId);
        if (solicitation.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", solicitation.get().getStatus().toString());  // Devuelve el estado como parte de un objeto JSON
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Solicitud no encontrada"));
    }

    @GetMapping("/accepted-advisers")
    public ResponseEntity<List<Adviser>> getAcceptedAdvisers(@RequestParam Long studentId) {
        List<Adviser> acceptedAdvisers = solicitationService.getAcceptedAdvisersByStudent(studentId);
        return ResponseEntity.ok(acceptedAdvisers);
    }
}