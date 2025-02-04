package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.model.AuthResponse
import com.example.manga_ln_app.data.model.LoginRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(private val client: HttpClient) {
    private val baseUrl = "http://192.168.1.12:8080"

    suspend fun login(username: String, password: String): AuthResponse {
        val response = client.post("$baseUrl/user/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }
        val rawToken = response.body<String>()
        return AuthResponse.fromRawToken(rawToken)
    }

    suspend fun register(username: String, password: String): AuthResponse {
        val response = client.post("$baseUrl/user/register") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }
        val rawToken = response.body<String>()
        return AuthResponse.fromRawToken(rawToken)
    }
}