package com.vocacional.orientacionvocacional.dto;


import lombok.Data;

@Data
public class OptionDTO {
    private Long id;
    private String text;
    private Integer score;

    // Constructor
    public OptionDTO() {
    }

}
