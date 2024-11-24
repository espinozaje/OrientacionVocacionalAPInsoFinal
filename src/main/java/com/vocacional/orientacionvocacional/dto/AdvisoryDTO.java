package com.vocacional.orientacionvocacional.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AdvisoryDTO {

    private Integer id;


    private String link;


    private String name;


    private LocalDate date;


    private LocalTime time;


    private Integer studentId;


    private Integer adviserId;
}
