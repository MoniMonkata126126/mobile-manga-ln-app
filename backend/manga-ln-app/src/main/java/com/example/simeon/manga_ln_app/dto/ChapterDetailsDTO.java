package com.example.simeon.manga_ln_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterDetailsDTO {
    private int id;
    private String name;
    private List<String> imageURLs;
    private List<CommentDetailsDTO> commentDetailsList;
}
