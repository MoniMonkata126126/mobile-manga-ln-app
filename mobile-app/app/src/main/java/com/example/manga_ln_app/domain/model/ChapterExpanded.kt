package com.example.manga_ln_app.domain.model

data class ChapterExpanded(
    val id: Int,
    val name: String,
    val chapterImagesURLs: List<String>,
    val comments: List<Comment>
)
