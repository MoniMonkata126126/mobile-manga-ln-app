package com.example.simeon.manga_ln_app.dto;

import com.example.simeon.manga_ln_app.models.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private Role role;
}
