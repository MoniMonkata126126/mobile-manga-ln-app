package com.example.manga_ln_app.presentation.chapter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ChapterPageViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(ChapterPageState())
    val state = _state.asStateFlow()

    init {
        observeCurrentChapter(savedStateHandle["id"])
    }

    fun onAction(action: ChapterPageAction){
        when(action){
            is ChapterPageAction.OnCommentPost -> {
                postComment()
            }
            ChapterPageAction.OnBackClick -> {

            }
            is ChapterPageAction.OnCommentChange -> {
                _state.update {
                    it.copy(currentComment = action.text)
                }
            }
        }
    }

    private fun observeCurrentChapter(id: Int?) {
        val keyWord = "content-library/LN"
        id?.let {
            chapterRepository.getChapterDetails(id).onEach { chapter ->
                _state.update {
                    it.copy(
                        currentChapter = chapter,
                        isLoading = false
                    )
                }
                if (chapter != null) {
                    if (chapter.chapterImagesURLs.size == 1) {
                        println("Passes first if() for size")
                        println("This is the keyword: $keyWord")
                        println("This is the url: ${chapter.chapterImagesURLs.first()}")
                        println("This url contains the keyword: ${chapter.chapterImagesURLs.first().contains(keyWord)}")

                        if (chapter.chapterImagesURLs.first().contains(keyWord)) {
                            println("Passes second if() for index and letter")

                            viewModelScope.launch {
                                val textContent = fetchTextFile(chapter.chapterImagesURLs.first())

                                _state.update {
                                    it.copy(
                                        currentChapterText = textContent ?: "",
                                        genre = "ln"
                                    )
                                }
                                println("Updates state?")
                            }
                        }
                    }
                }
                println(state.value.genre)
            }.launchIn(viewModelScope)
        }
    }

    private fun postComment(){
        viewModelScope.launch {
            state.value.currentChapter?.let { chapterRepository.postComment(state.value.currentComment, it.id) }
        }
        _state.update {
            it.copy(currentComment = "")
        }
    }

    private suspend fun fetchTextFile(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}