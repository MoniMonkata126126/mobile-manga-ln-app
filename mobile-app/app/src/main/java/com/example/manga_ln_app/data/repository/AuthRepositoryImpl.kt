package com.example.manga_ln_app.data.repository

import com.example.manga_ln_app.data.remote.AuthApi
import com.example.manga_ln_app.domain.repository.AuthRepository
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val credStorage: CredentialsStorage
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = api.login(username, password)
            println("Login response token: ${response.token}")
            if (response.token.isBlank() || response.token == "null token") {
                Result.failure(Exception("Invalid token received"))
            } else {
                credStorage.saveUsername(response.username)
                credStorage.saveRole(response.role)
                credStorage.saveToken(response.token)
                Result.success(response.role.name)
            }
        } catch (e: Exception) {
            println("Login error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String): Result<String> {
        return try {
            val response = api.register(username, password)
            if (response.token.isBlank() || response.token == "null token") {
                Result.failure(Exception("Invalid token received"))
            } else {
                credStorage.saveUsername(response.username)
                credStorage.saveRole(response.role)
                credStorage.saveToken(response.token)
                Result.success(response.role.name)
            }
        } catch (e: Exception) {
            println("Register error: ${e.message}")
            Result.failure(e)
        }
    }
}