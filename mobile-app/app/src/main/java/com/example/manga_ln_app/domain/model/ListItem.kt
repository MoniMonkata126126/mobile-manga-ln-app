package com.example.manga_ln_app.domain.model

sealed interface ListItem {
    class Content (
        val id: Int,
        val title: String,
        val author: String,
        val type: Type
    ) : ListItem

    data class Chapter(
        val id: Int
    ) : ListItem
}