package com.example.simeon.manga_ln_app.models;

import jakarta.persistence.*;

public class Comment {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    private String content;
}
