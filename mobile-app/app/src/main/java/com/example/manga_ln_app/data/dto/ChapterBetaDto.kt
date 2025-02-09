package com.example.manga_ln_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterBetaDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("contentName") val contentName: String
)
