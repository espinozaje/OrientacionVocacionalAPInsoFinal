package com.vocacional.orientacionvocacional.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admin")
@Data
public class Admin extends User{
    private boolean needsPasswordChange = true;
}
