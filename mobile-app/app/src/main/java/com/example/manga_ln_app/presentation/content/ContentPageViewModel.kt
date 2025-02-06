package com.example.manga_ln_app.presentation.content

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContentPageViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(ContentPageState())
    val state = _state.asStateFlow()

    fun onAction(action: ContentPageAction) {
        when(action) {
            is ContentPageAction.OnChapterClick -> {
            }
        }
    }
}