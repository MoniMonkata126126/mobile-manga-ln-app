package com.example.manga_ln_app.domain.model

sealed interface ListItem {

    data class Content (
        val id: Int,
        val title: String,
        val author: String,
        val type: Type
    ) : ListItem

    data class Chapter(
        val id: Int,
        val name: String
    ) : ListItem
}