package com.example.manga_ln_app.presentation.chapter

sealed interface ChapterPageAction {
    data object OnCommentPost : ChapterPageAction
    data class OnCommentChange(val text: String) : ChapterPageAction
    data object OnBackClick: ChapterPageAction
}