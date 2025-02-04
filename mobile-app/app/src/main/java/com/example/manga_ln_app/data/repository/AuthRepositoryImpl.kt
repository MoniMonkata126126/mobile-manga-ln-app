package com.example.manga_ln_app.data.repository

import com.example.manga_ln_app.data.local.TokenManager
import com.example.manga_ln_app.data.remote.AuthApi
import com.example.manga_ln_app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = api.login(username, password)
            tokenManager.saveToken(response.token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String): Result<Unit> {
        return try {
            val response = api.register(username, password)
            tokenManager.saveToken(response.token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.getToken().map { it != null }
    }
} 