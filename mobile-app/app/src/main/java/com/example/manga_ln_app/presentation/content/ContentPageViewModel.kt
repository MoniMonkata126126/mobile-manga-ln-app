package com.example.manga_ln_app.presentation.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.repository.ContentRepository
import com.example.manga_ln_app.presentation.ContentController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ContentPageViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val contentController: ContentController
) : ViewModel() {


    private var observeChaptersJob: Job? = null

    val content = contentController.content

    private val _state = MutableStateFlow(ContentPageState())
    val state = _state
        .onStart {
            contentController.content.value?.let { observeChapters(it.id) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )


    fun onAction(action: ContentPageAction) {
        when(action) {

            is ContentPageAction.OnChapterClick -> {
            }

            ContentPageAction.OnBackClick -> {
            }
        }
    }

    private fun observeChapters(id: Int){
        observeChaptersJob?.cancel()
        observeChaptersJob = contentRepository
            .getChaptersPerContent(id).onEach {
                chapterList ->
                _state.update { it.copy(
                    chapters = chapterList,
                    isLoading = false
                ) }
            }
            .launchIn(viewModelScope)
    }
}