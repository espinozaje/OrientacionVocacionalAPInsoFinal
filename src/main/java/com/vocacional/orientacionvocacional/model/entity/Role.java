package com.vocacional.orientacionvocacional.model.entity;

import com.vocacional.orientacionvocacional.model.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false,unique = true)
    private ERole name;
}
