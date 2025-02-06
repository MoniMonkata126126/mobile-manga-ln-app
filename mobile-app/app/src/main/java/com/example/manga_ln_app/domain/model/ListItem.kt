package com.example.manga_ln_app.domain.model

import kotlinx.serialization.Serializable

sealed interface ListItem {

    @Serializable
    class Content (
        val id: Int,
        val title: String,
        val author: String,
        val type: Type
    ) : ListItem

    @Serializable
    data class Chapter(
        val id: Int,
        val name: String
    ) : ListItem
}