package com.vocacional.orientacionvocacional.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "advisories")
@Data
public class Advisory {

    @Id
    private Integer id;

    @NotBlank(message = "El link es obligatorio")
    private String link;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "adviser_id", nullable = false)
    private Adviser adviser;
}
