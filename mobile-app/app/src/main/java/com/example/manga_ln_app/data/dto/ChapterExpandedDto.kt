package com.example.manga_ln_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterExpandedDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("imageURLs") val chapterImagesURLs: List<String>,
    @SerialName("commentDetailsList") val comments: List<CommentDto>
)
