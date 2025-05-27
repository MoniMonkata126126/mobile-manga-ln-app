package com.example.simeon.manga_ln_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCredentialsDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
