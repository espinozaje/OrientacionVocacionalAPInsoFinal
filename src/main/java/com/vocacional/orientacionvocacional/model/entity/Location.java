package com.vocacional.orientacionvocacional.model.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "location")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String region;
    private String country;
}
