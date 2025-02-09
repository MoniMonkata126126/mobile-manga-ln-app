package com.example.manga_ln_app.domain.repository

import android.net.Uri
import com.example.manga_ln_app.domain.model.ChapterBeta
import com.example.manga_ln_app.domain.model.ChapterExpanded
import com.example.manga_ln_app.domain.model.CommentBeta
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    fun getChapterDetails(id: Int): Flow<ChapterExpanded?>
    suspend fun postComment(text: String, chapId: Int)
    suspend fun postChapter(chapContName: String, chapName: String, contentUris: List<Uri>)
    fun getBetaChapters(): Flow<List<ChapterBeta>>
    fun getBetaComments(): Flow<List<CommentBeta>>
    suspend fun approveChapter(id: Int)
    suspend fun approveComment(id: Int)
}