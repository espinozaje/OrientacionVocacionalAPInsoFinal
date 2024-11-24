package com.vocacional.orientacionvocacional.dto;

import lombok.Data;

import java.util.List;


@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private AreaDTO area;
    private List<OptionDTO> options;

    public QuestionDTO() {

    }

}
