package com.vocacional.orientacionvocacional.model.entity;

import com.vocacional.orientacionvocacional.model.enums.SolicitationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitations")
@Data
public class Solicitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "adviser_id", nullable = false)
    private Adviser adviser;

    @Enumerated(EnumType.STRING)
    private SolicitationStatus status = SolicitationStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
}
