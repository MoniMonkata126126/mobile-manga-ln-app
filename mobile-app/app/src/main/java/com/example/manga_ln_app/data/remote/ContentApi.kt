package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.model.AuthResponse
import com.example.manga_ln_app.data.dto.Content
import com.example.manga_ln_app.domain.model.Type
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import com.example.manga_ln_app.domain.repository.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first

class ContentApi(
    private val client: HttpClient,
    private val credStorage: CredentialsStorage
) {
    private val baseUrl = "http://192.168.1.12:8080"

    suspend fun getContentByType(type: Type): List<Content> {
        try {
            val response = client.get("$baseUrl/content/type?type=${type.name}") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    return response.body<List<Content>>()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Login error: ${e.message}")
            throw e
        }
    }

    suspend fun getContentByQuery(query: String, type: Type): List<Content> {
        try {
            val response = client.get("$baseUrl/content?q=$query&type=${type.name}") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    return response.body<List<Content>>()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Login error: ${e.message}")
            throw e
        }
    }
}