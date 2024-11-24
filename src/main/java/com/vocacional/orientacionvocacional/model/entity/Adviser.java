package com.vocacional.orientacionvocacional.model.entity;


import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "adviser")
@Data
public class Adviser extends User{
    @NotBlank(message = "La specialty es obligatoria")
    private String specialty;

    private boolean needsPasswordChange = true;
}
