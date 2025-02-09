package com.example.manga_ln_app.presentation.post_content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.model.Type
import com.example.manga_ln_app.domain.repository.ChapterRepository
import com.example.manga_ln_app.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostPageViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PostPageState())
    val state = _state.asStateFlow()


    fun onAction(action: PostPageAction){
        when(action){
            is PostPageAction.OnChapContentNameChange -> {
                _state.update {
                    it.copy(chapContName = action.chapContentName)
                }
            }
            is PostPageAction.OnChapterNameChange -> {
                _state.update {
                    it.copy(chapName = action.chapterName)
                }
            }
            is PostPageAction.OnContentNameChange -> {
                _state.update {
                    it.copy(contentName = action.contentName)
                }
            }
            is PostPageAction.OnSelectedDropdownOneChange -> {
                _state.update {
                    it.copy( contentType = action.type )
                }
            }
            is PostPageAction.OnSelectedDropdownTwoChange -> {
                _state.update {
                    it.copy( chapContType = action.type )
                }
            }
            PostPageAction.OnPostButtonOneClick -> {
                postContent()
            }
            PostPageAction.OnPostButtonTwoClick -> {
                postChapter()
            }
            is PostPageAction.OnChangeSelectedUris -> {
                _state.update {
                    it.copy(selectedUris = action.selectedUris)
                }
            }
        }
    }

    private fun postContent() {
        _state.update {
            it.copy(isUploading = true)
        }
        viewModelScope.launch {
            contentRepository.postContent(
                contentName =  state.value.contentName,
                contentType =  state.value.contentType)

            _state.update {
                it.copy(
                    isUploading = false,
                    contentName = "",
                )
            }
        }
    }

    private fun postChapter() {
        _state.update {
            it.copy(isUploading = true)
        }
        viewModelScope.launch {
            chapterRepository.postChapter(
                chapContName =  state.value.chapContName,
                chapName =  state.value.chapName,
                contentUris =  state.value.selectedUris)

            _state.update {
                it.copy(isUploading = false,
                    chapContName = "",
                    chapName = "",
                    selectedUris = emptyList()
                )
            }
        }
    }
}