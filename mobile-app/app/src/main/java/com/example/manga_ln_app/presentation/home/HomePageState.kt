package com.example.manga_ln_app.presentation.home

import com.example.manga_ln_app.domain.model.ListItem

data class HomePageState (
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val searchResultsM: List<ListItem.Content> = emptyList(),
    val searchResultsLN: List<ListItem.Content> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)