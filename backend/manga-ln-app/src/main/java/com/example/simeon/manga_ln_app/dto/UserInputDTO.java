package com.example.simeon.manga_ln_app.dto;

import com.example.simeon.manga_ln_app.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserInputDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
    private List<Integer> comments;
    private List<String> authoredContent;
}
