package com.example.manga_ln_app.data.model

import com.example.manga_ln_app.domain.repository.Role
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val username: String,
    val role: Role,
    val token: String
)