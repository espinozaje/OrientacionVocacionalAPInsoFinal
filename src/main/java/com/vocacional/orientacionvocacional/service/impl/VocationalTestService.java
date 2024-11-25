package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.VocationalTest;
import com.vocacional.orientacionvocacional.model.entity.*;
import com.vocacional.orientacionvocacional.repository.AreaRepository;
import com.vocacional.orientacionvocacional.repository.TestResultRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VocationalTestService {
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;


    @Transactional
    public Map<String, Object> calculateTestResultRegister(VocationalTest test, Integer userId) {
        Map<Long, Integer> areaScores = new HashMap<>();
        int totalInterested = 0, totalNotInterested = 0;

        // Calcular puntuación por área
        for (Question question : test.getQuestions()) {
            Long areaId = question.getArea().getId();
            if (question.getSelectedOption() != null) {
                if (question.getSelectedOption().getScore() == 1) {
                    areaScores.put(areaId, areaScores.getOrDefault(areaId, 0) + 1);
                    totalInterested++;
                } else {
                    totalNotInterested++;
                }
            }
        }

        // Casos de error
        if (totalInterested == test.getQuestions().size() ||
                totalNotInterested == test.getQuestions().size() ||
                totalInterested == 1 || totalNotInterested == 1) {
            return Map.of("error", "No se pudo determinar tu resultado debido a respuestas insuficientes o poco específicas.");
        }

        // Determinar el área con mayor puntaje
        Long recommendedAreaId = areaScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (recommendedAreaId == null || areaScores.get(recommendedAreaId) <= 1) {
            return Map.of("error", "No se pudo determinar tu resultado debido a puntajes bajos.");
        }

        // Recuperar datos de la base
        Area area = areaRepository.findById(recommendedAreaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));
        List<Career> careers = new ArrayList<>(area.getCareers());

        // Guardar los resultados
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setRecommendedArea(area);
        testResult.setRecommendedCareers(careers);
        testResult.setDateRealization(LocalDateTime.now());
        testResultRepository.save(testResult);

        // Respuesta final
        return Map.of(
                "area", area.getName(),
                "careers", careers.stream().map(Career::getName).collect(Collectors.toList())
        );
    }




}
