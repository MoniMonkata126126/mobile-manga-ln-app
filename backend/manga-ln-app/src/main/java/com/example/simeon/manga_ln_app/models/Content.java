package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "content")
public class Content {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    @OneToMany(mappedBy = "content",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Chapter> chapters;

    @NotNull
    @OneToMany(mappedBy = "content",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<ChapterBeta> chaptersBeta;

    @NotNull
    @ManyToMany(mappedBy = "likedContent")
    private List<User> userLikes;
}
