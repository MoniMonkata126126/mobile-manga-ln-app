package com.example.manga_ln_app.presentation.chapter

import com.example.manga_ln_app.domain.model.ChapterExpanded

data class ChapterPageState(
    val currentComment: String = "",
    val currentChapter: ChapterExpanded? = null,
    val currentChapterText: String = "",
    val genre: String = "manga",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
