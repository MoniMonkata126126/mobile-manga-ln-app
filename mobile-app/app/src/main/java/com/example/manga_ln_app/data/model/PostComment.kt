package com.example.manga_ln_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostComment(
    @SerialName("username") val username: String,
    @SerialName("id") val chapterId: Int,
    @SerialName("text") val text: String
)
