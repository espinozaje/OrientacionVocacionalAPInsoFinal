package com.vocacional.orientacionvocacional.Controller;


import com.vocacional.orientacionvocacional.dto.PlanDTO;
import com.vocacional.orientacionvocacional.service.impl.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<PlanDTO> getPlans() {
        return planService.getAllPlans();
    }
}