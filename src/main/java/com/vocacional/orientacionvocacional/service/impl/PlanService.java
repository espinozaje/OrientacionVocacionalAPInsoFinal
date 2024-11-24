package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.dto.PlanDTO;
import com.vocacional.orientacionvocacional.model.entity.Plan;
import com.vocacional.orientacionvocacional.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public List<PlanDTO> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return plans.stream()
                .map(plan -> new PlanDTO(plan.getId(),plan.getName(), plan.getPrice(), plan.getDescription()))
                .collect(Collectors.toList());
    }
}
