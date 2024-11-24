package com.vocacional.orientacionvocacional.model.entity;
import com.vocacional.orientacionvocacional.model.enums.Plan;
import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "student")
public class Student extends User{
    private String verificationCode;
    private boolean verified = false;
    @Enumerated(EnumType.STRING)
    private Plan plan;
}
