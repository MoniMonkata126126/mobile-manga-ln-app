package com.example.manga_ln_app.presentation.home

import com.example.manga_ln_app.domain.model.ListItem

sealed interface HomePageAction {
    data class OnSearchQueryChange(val query: String): HomePageAction
    data class OnContentClick(val content: ListItem.Content): HomePageAction
    data class OnTabSelected(val index: Int): HomePageAction
}