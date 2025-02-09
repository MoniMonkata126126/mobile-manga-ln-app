package com.example.manga_ln_app.presentation.chapter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private fun observeCurrentChapter(id: Int?){
        id?.let {
            chapterRepository.getChapterDetails(id).onEach { chapter ->
                _state.update {
                    it.copy(
                        currentChapter = chapter,
                        isLoading = false
                    )
                }
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
}