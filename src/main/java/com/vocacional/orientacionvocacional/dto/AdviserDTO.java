package com.vocacional.orientacionvocacional.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdviserDTO {
    private Integer id;
    private String img_profile;
    private String firstName;
    private String lastName;
    private String email;

    private String password;
    private String specialty;
}
