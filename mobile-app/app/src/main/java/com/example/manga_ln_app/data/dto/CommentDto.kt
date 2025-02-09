package com.example.manga_ln_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("id") val id: Int,
    @SerialName("author") val author: String,
    @SerialName("text") val text: String
)
