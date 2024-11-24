package com.vocacional.orientacionvocacional.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlanDTO {
    private Integer id;
    private String name;
    private Float price;
    private String description;
}
