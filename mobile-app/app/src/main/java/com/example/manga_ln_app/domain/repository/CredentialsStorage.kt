package com.example.manga_ln_app.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CredentialsStorage {

    val username: StateFlow<String?>
    val role: StateFlow<Role?>
    val token: StateFlow<String?>

    fun saveToken(token: String)

    fun getToken(): Flow<String?>

    fun saveUsername(username: String)

    fun getUsername(): Flow<String?>

    fun saveRole(role: Role)

    fun getRole(): Flow<Role?>
}

enum class Role{
    READER,
    AUTHOR,
    MODERATOR,
    ADMIN
}