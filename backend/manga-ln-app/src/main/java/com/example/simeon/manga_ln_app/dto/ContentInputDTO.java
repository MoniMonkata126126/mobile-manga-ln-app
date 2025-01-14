package com.example.simeon.manga_ln_app.dto;

import com.example.simeon.manga_ln_app.models.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContentInputDTO {
    @NotNull
    private ContentType contentType;
    @NotBlank
    private String name;
    @NotBlank
    private String author;
}
