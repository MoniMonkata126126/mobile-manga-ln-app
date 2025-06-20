package com.example.manga_ln_app.domain.repository

import com.example.manga_ln_app.domain.model.ContentBeta
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    suspend fun searchContent(query: String, type: Type): Result<List<ListItem.Content>>
    fun getContent(type: Type): Flow<List<ListItem.Content>>
    fun getChaptersPerContent(id: Int): Flow<List<ListItem.Chapter>>
    suspend fun postContent(contentName: String, contentType: Type)
    fun getBetaContent(): Flow<List<ContentBeta>>
    suspend fun approveContent(id: Int)
    suspend fun getContentByAuthor(): List<ListItem.Content>
}