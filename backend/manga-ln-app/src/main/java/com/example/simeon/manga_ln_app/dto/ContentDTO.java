package com.example.simeon.manga_ln_app.dto;

import com.example.simeon.manga_ln_app.models.ContentType;
import lombok.Data;

@Data
public class ContentDTO {
    private int id;
    private ContentType contentType;
    private String name;
    private String authorUsername;
}
