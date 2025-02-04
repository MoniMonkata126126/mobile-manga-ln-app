package com.example.manga_ln_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
) {
    companion object {
        fun fromRawToken(token: String) = AuthResponse(token)
    }
} 