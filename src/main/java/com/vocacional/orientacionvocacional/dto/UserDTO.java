package com.vocacional.orientacionvocacional.dto;

import com.vocacional.orientacionvocacional.model.enums.ERole;
import lombok.Data;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ERole role;
}