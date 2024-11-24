package com.vocacional.orientacionvocacional.dto;

import com.vocacional.orientacionvocacional.model.entity.Career;
import lombok.Data;

import java.util.List;


@Data
public class AreaDTO {
    private Long id;
    private String name;
    private List<Career> careers;
    // Constructor
    public AreaDTO() {}

}
