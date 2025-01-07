package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;
}
