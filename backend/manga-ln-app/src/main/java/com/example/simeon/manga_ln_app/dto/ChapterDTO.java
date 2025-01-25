package com.example.simeon.manga_ln_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterDTO {
    private String name;
    private List<CommentDTO> comments;
    private String contentName;
}
