package com.example.manga_ln_app.presentation

import com.example.manga_ln_app.domain.model.ListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentController @Inject constructor() {
    private val _content: MutableStateFlow<ListItem.Content?> = MutableStateFlow(null)
    val content  = _content.asStateFlow()

    fun changeContent(content: ListItem.Content) {
        _content.update {
            content
        }
    }
}