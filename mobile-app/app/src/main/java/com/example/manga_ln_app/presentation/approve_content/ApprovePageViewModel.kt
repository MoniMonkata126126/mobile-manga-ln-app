package com.example.manga_ln_app.presentation.approve_content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.repository.ChapterRepository
import com.example.manga_ln_app.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class ApprovePageViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
): ViewModel(){

    private val _state = MutableStateFlow(ApprovePageState())
    val state = _state.asStateFlow()

    init {
        getContents()
        getChapters()
        getComments()
    }

    fun onAction(action: ApprovePageAction){
        when(action){
            is ApprovePageAction.OnApproveChapter -> {
                removeChapterItem(action.id)
            }
            is ApprovePageAction.OnApproveComment -> {
                removeCommentItem(action.id)
            }
            is ApprovePageAction.OnApproveContent -> {
                removeContentItem(action.id)
            }
        }
    }


    private fun removeContentItem(id: Int) {
        viewModelScope.launch {
            contentRepository.approveContent(id)
            _state.update { currentState ->
                currentState.copy(
                    contentsBeta = currentState.contentsBeta.filterNot { it.id == id }
                )
            }
        }
    }

    private fun removeChapterItem(id: Int) {

        viewModelScope.launch {
            chapterRepository.approveChapter(id)
            _state.update { currentState ->
                currentState.copy(
                    chaptersBeta = currentState.chaptersBeta.filterNot { it.id == id }
                )
            }
        }
    }

    private fun removeCommentItem(id: Int) {

        viewModelScope.launch {
            chapterRepository.approveComment(id)
            _state.update { currentState ->
                currentState.copy(
                    commentsBeta = currentState.commentsBeta.filterNot { it.id == id }
                )
            }
        }
    }

    private fun getChapters(){
        chapterRepository.getBetaChapters()
            .onEach { chaptersList ->
                println("Received chapters: $chaptersList")
                _state.update {
                    it.copy(chaptersBeta = chaptersList)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getContents(){
        contentRepository.getBetaContent()
            .onEach { contentsList ->
                println("Received contents: $contentsList")
                _state.update {
                    it.copy(contentsBeta = contentsList)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getComments(){
        chapterRepository.getBetaComments()
            .onEach { commentsList ->
                println("Received comments: $commentsList")
                _state.update {
                    it.copy(commentsBeta = commentsList)
                }
            }
            .launchIn(viewModelScope)
    }
}