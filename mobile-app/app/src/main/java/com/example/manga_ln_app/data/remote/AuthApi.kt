package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.model.AuthResponse
import com.example.manga_ln_app.data.model.LoginRequest
import com.example.manga_ln_app.domain.repository.Role
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(private val client: HttpClient) {
    private val baseUrl = "http://192.168.95.90:8080"

    suspend fun login(username: String, password: String): AuthResponse {
        try {
            val response = client.post("$baseUrl/user/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val parsedResponse = response.body<Map<String, String>>()
                    println("Parsed auth response: $parsedResponse")
                    return AuthResponse(
                        username = parsedResponse["username"] ?: throw IllegalStateException("No username in response"),
                        role = Role.entries.find { it.name == parsedResponse["role"] } ?: throw IllegalStateException("No role in response"),
                        token = parsedResponse["token"] ?: throw IllegalStateException("No token in response")
                    )
                }
                HttpStatusCode.Forbidden -> throw Exception("Invalid credentials")
                HttpStatusCode.NotFound -> throw Exception("User not found")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Login error: ${e.message}")
            throw e
        }
    }

    suspend fun register(username: String, password: String): AuthResponse {
        try {
            val response = client.post("$baseUrl/user/register") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val parsedResponse = response.body<Map<String, String>>()
                    return AuthResponse(
                        username = parsedResponse["username"] ?: throw IllegalStateException("No username in response"),
                        role = Role.entries.find { it.name == parsedResponse["role"] } ?: throw IllegalStateException("No role in response"),
                        token = parsedResponse["token"] ?: throw IllegalStateException("No token in response")
                    )
                }
                HttpStatusCode.Conflict -> throw Exception("Username already exists")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Register error: ${e.message}")
            throw e
        }
    }
}