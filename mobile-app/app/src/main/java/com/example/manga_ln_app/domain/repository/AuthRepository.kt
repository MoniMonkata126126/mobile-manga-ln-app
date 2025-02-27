package com.example.manga_ln_app.domain.repository


interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun register(username: String, password: String): Result<String>
}