package com.example.manga_ln_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    @SerialName("id") val id: Int,
    @SerialName("contentType") val contentType: String,
    @SerialName("name") val title: String,
    @SerialName("authorUsername") val author: String

)
