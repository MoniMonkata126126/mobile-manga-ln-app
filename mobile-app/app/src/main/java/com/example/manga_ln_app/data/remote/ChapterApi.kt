package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.dto.ChapterExpandedDto
import com.example.manga_ln_app.data.dto.ChapterInfo
import com.example.manga_ln_app.data.model.AuthResponse
import com.example.manga_ln_app.data.model.PostComment
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import com.example.manga_ln_app.domain.repository.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChapterApi @Inject constructor(
    private val client: HttpClient,
    private val credStorage: CredentialsStorage
) {
    private val baseUrl = "http://192.168.1.12:8080"

    suspend fun getChapterDetails(id: Int): ChapterExpandedDto {
        try {
            println(id)
            val response = client.get("$baseUrl/chapter/$id") {
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

    suspend fun postComment(text: String, chapId: Int) {
        try {
            val response = client.post("$baseUrl/comment") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
                setBody(
                    PostComment(
                        username = credStorage.getUsername().first() ?: "",
                        chapterId = chapId,
                        text = text
                    )
                )
            }
            when(response.status){
                HttpStatusCode.OK -> {
                    val parsedResponse = response.body<String>()
                    println("Parsed auth response: $parsedResponse")
                }
                HttpStatusCode.Forbidden -> throw Exception("Invalid credentials")
                HttpStatusCode.BadRequest -> throw Exception("Bad request")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception){
            println("Error posting comment: " + e.message)
        }
    }
}