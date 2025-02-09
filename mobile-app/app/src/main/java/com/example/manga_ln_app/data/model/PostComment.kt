package com.example.manga_ln_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostComment(
    @SerialName("authorName") val username: String,
    @SerialName("chapterId") val chapterId: Int,
    @SerialName("text") val text: String
)
