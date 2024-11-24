package com.vocacional.orientacionvocacional.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_area_id")
    private Area recommendedArea;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "test_result_career",
            joinColumns = @JoinColumn(name = "test_result_id"),
            inverseJoinColumns = @JoinColumn(name = "career_id"))
    private List<Career> recommendedCareers = new ArrayList<>();

    // Fecha del test, por si el usuario realiza múltiples tests
    private LocalDateTime dateRealization;
}
