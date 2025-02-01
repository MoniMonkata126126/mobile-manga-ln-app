package com.example.simeon.manga_ln_app.dto;

import com.example.simeon.manga_ln_app.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCredentialsDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
