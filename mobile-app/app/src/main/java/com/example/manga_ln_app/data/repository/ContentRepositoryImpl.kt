package com.example.manga_ln_app.data.repository

import com.example.manga_ln_app.data.remote.ContentApi
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type
import com.example.manga_ln_app.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val contentApi: ContentApi
): ContentRepository {
    override suspend fun searchContent(query: String, type: Type): Result<List<ListItem.Content>> {
        return try {
            val content = contentApi.getContentByQuery(query, type).filter {
                it.title.contains(query, ignoreCase = true) ||
                it.author.contains(query, ignoreCase = true)
            }.map { dto ->
                ListItem.Content(
                    id = dto.id,
                    title = dto.title,
                    author = dto.author,
                    type = Type.entries.find { it.name == dto.contentType } ?: Type.MANGA
                )
            }
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getContent(type: Type): Flow<List<ListItem.Content>> = flow {
        try {
            val content = contentApi.getContentByType(type).map { dto ->
                ListItem.Content(
                    id = dto.id,
                    title = dto.title,
                    author = dto.author,
                    type = Type.entries.find { it.name == dto.contentType } ?: Type.MANGA
                )
            }
            emit(content)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}