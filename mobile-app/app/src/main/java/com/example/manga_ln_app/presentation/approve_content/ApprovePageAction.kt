package com.example.manga_ln_app.presentation.approve_content

sealed interface ApprovePageAction {
    data class OnApproveContent(val id: Int): ApprovePageAction
    data class OnApproveChapter(val id: Int): ApprovePageAction
    data class OnApproveComment(val id: Int): ApprovePageAction
}