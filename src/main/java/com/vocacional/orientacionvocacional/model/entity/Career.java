package com.vocacional.orientacionvocacional.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "career")
@Data
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    @JsonBackReference
    private Area area;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private String description;
    private String priceMonthly;
    private String img;


}