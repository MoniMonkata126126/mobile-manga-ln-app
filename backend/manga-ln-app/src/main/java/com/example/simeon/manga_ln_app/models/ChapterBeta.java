package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "chapter_beta")
public class ChapterBeta {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    private Content content;

    @NotNull
    @OneToMany(mappedBy = "chapterBeta",
            cascade = CascadeType.PERSIST)
    private List<ChapterContent> chapterContentList;

    @NotNull
    private int chapterContentsCount;
}
