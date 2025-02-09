package com.example.manga_ln_app.presentation.approve_content

import com.example.manga_ln_app.domain.model.ChapterBeta
import com.example.manga_ln_app.domain.model.CommentBeta
import com.example.manga_ln_app.domain.model.ContentBeta

data class ApprovePageState(
    val contentsBeta: List<ContentBeta> = emptyList(),
    val chaptersBeta: List<ChapterBeta> =  emptyList(),
    val commentsBeta: List<CommentBeta> = emptyList()
)
