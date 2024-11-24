package com.vocacional.orientacionvocacional.Controller;
import com.vocacional.orientacionvocacional.Mapper.QuestionMapper;
import com.vocacional.orientacionvocacional.dto.QuestionDTO;
import com.vocacional.orientacionvocacional.model.entity.*;
import com.vocacional.orientacionvocacional.repository.QuestionRepository;
import com.vocacional.orientacionvocacional.repository.TestResultRepository;
import com.vocacional.orientacionvocacional.service.impl.AreaService;
import com.vocacional.orientacionvocacional.service.impl.JwtUtilService;
import com.vocacional.orientacionvocacional.service.impl.VocationalTestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/vocational-test")
@CrossOrigin(origins = "https://orientacion-vocacional.vercel.app/")
public class VocationalController {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private VocationalTestService vocationalTestService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private JwtUtilService jwtUtilService;


    @PostMapping("/submit-register")
    public ResponseEntity<Map<String, Object>> submitVocationalTest(
            @RequestBody VocationalTest test,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer userId = jwtUtilService.extractUserId(token);

        Map<String, Object> result = vocationalTestService.calculateTestResultRegister(test, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results/{userId}")
    public ResponseEntity<List<Career>> getRecommendedCareers(@PathVariable Long userId) {
        TestResult result = testResultRepository.findFirstByUserIdOrderByDateRealizationDesc(userId)
                .orElseThrow(() -> new RuntimeException("No se encontraron resultados para el usuario"));

        return ResponseEntity.ok(result.getRecommendedCareers());
    }

    @GetMapping("/questions")
    public List<QuestionDTO> getQuestions(){
        List<Question> questions = questionRepository.findAll();

        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/areas")
    public List<Area> getAreas(){
        return areaService.findAllArea();
    }
}

