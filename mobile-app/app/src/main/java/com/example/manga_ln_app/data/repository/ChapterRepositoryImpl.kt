package com.example.manga_ln_app.data.repository

import android.net.Uri
import com.example.manga_ln_app.data.remote.ChapterApi
import com.example.manga_ln_app.domain.model.ChapterExpanded
import com.example.manga_ln_app.domain.model.Comment
import com.example.manga_ln_app.domain.repository.ChapterRepository
import com.example.manga_ln_app.domain.usecase.FileReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
    private val chapterApi: ChapterApi,
    private val fileReader: FileReader
) : ChapterRepository {
    override fun getChapterDetails(id: Int): Flow<ChapterExpanded?> = flow {
        try {
            val response = chapterApi.getChapterDetails(id)
            val chapterExpanded = ChapterExpanded(
                id = response.id,
                name = response.name,
                chapterImagesURLs = response.chapterImagesURLs,
                comments = response.comments.map { dto ->
                    Comment(
                        id = dto.id,
                        author = dto.author,
                        text = dto.text
                    )
                }
            )
            emit(chapterExpanded)
        } catch (e: Exception){
            emit(null)
        }
    }

    override suspend fun postComment(text: String, chapId: Int) {
        chapterApi.postComment(text = text, chapId = chapId)
    }

    override suspend fun postChapter(chapContName: String, chapName: String, contentUris: List<Uri>){
        val infos = contentUris.map {
            fileReader.uriToFileInfo(it)
        }
        chapterApi.postChapter(infos, chapContName, chapName)
    }
}