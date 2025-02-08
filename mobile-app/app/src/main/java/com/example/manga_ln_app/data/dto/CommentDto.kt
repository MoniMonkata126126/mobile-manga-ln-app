package com.example.manga_ln_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("authorName") val author: String,
    @SerialName("chapterId") val id: Int,
    @SerialName("text") val text: String
)
