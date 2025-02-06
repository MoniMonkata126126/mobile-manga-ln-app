package com.example.manga_ln_app.presentation.content

import com.example.manga_ln_app.domain.model.ListItem

data class ContentPageState(
    val id: Int? = null,
    val searchResults: List<ListItem.Chapter> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
