package com.vocacional.orientacionvocacional.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "availability")
@Data
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adviser_id", referencedColumnName = "id", nullable = false)
    private Adviser adviser;

    @Column(nullable = false)
    private String dayOfWeek;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
}