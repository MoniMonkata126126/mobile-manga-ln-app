package com.example.simeon.manga_ln_app.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String author;
    private String chapterName;
    private String text;
}
