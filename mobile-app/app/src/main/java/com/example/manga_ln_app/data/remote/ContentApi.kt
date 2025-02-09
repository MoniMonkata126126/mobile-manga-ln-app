package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.dto.ChapterInfo
import com.example.manga_ln_app.data.dto.Content
import com.example.manga_ln_app.domain.model.Type
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
            println("Request error: ${e.message}")
            throw e
        }
    }

    suspend fun getContentByQuery(query: String, type: Type): List<Content> {
        try {
            val response = client.get("$baseUrl/content") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
                parameter("q", query)
                parameter("type", type.name)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    return response.body<List<Content>>()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
            throw e
        }
    }

    suspend fun getChapterInfoByContentId(id: Int): List<ChapterInfo> {
        try {
            println(id)
            val response = client.get("$baseUrl/content/$id") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    response.body()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
            throw e
        }
    }

    suspend fun postContent(contentName: String, contentType: Type) {
        try {
            val response = client.post("$baseUrl/content") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
                setBody(
                    mapOf(
                        "contentType" to contentType.name,
                        "name" to contentName,
                        "author" to credStorage.username.value
                    )
                )
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    response.body()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
            throw e
        }
    }
}