package com.example.manga_ln_app.presentation.content

import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type

data class ContentPageState(
    val id: Int? = null,
    val chapters: List<ListItem.Chapter> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val type: Type? = null
)
