package com.example.manga_ln_app.presentation.content

import com.example.manga_ln_app.domain.model.ListItem

interface ContentPageAction {
    data class OnChapterClick(val chapter: ListItem.Chapter) : ContentPageAction
    data object OnBackClick: ContentPageAction
}