package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "chapter_content")
public class ChapterContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int displayOrder;

    @NotNull
    @Column(name = "content_url")
    private String contentURL;

    @ManyToOne
    private Chapter chapter;

    @ManyToOne
    private ChapterBeta chapterBeta;
}
