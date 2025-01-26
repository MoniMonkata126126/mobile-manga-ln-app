package com.example.simeon.manga_ln_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentBetaDTO {

    @NotBlank
    private String authorName;

    @NotNull
    private int chapterId;

    @NotBlank
    private String text;
}
