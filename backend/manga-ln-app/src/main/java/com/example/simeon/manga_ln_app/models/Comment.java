package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    @ManyToOne
    private Chapter chapter;

    @NotBlank
    private String text;
}
