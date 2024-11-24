package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.VocationalTest;
import com.vocacional.orientacionvocacional.model.entity.*;
import com.vocacional.orientacionvocacional.repository.AreaRepository;
import com.vocacional.orientacionvocacional.repository.TestResultRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public Map<String, Object> calculateTestResultRegister(VocationalTest test, Integer  userId) {
        Map<Long, Integer> areaScores = new HashMap<>();
        List<Career> careers = new ArrayList<>();

        // Calcular puntuación por área
        for (Question question : test.getQuestions()) {
            Long areaId = question.getArea().getId();
            if (question.getSelectedOption() != null && question.getSelectedOption().getScore() == 1) {
                areaScores.put(areaId, areaScores.getOrDefault(areaId, 0) + 1);
            }
        }

        Long recommendedAreaId = null;
        int maxScore = 0;

        for (Map.Entry<Long, Integer> entry : areaScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                recommendedAreaId = entry.getKey();
            }
        }

        if (recommendedAreaId != null) {
            Area areaObj = areaRepository.findById(recommendedAreaId).orElse(null);
            if (areaObj != null) {
                List<Career> carreras = new ArrayList<>(areaObj.getCareers()); // Obtener las carreras del área


                careers.addAll(carreras);


                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                TestResult testResult = new TestResult();
                testResult.setUser(user);
                testResult.setRecommendedArea(areaObj);
                testResult.setRecommendedCareers(carreras); // Aquí clonar la lista
                testResult.setDateRealization(LocalDateTime.now());

                testResultRepository.save(testResult);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("area", areaRepository.findById(recommendedAreaId).orElse(null).getName());
        response.put("careers", careers.stream().map(Career::getName).collect(Collectors.toList())); // Asegúrate de que 'careers' no esté vacía

        return response;
    }
}
