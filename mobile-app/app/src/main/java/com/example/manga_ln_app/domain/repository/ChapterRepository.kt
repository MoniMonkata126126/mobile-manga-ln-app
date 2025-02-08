package com.example.manga_ln_app.domain.repository

import com.example.manga_ln_app.domain.model.ChapterExpanded
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    fun getChapterDetails(id: Int): Flow<ChapterExpanded?>
    suspend fun postComment(text: String, chapId: Int)
}