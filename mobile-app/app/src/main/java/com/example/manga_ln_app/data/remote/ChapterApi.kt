package com.example.manga_ln_app.data.remote

import com.example.manga_ln_app.data.dto.ChapterBetaDto
import com.example.manga_ln_app.data.dto.ChapterExpandedDto
import com.example.manga_ln_app.data.dto.CommentBetaDto
import com.example.manga_ln_app.data.model.PostComment
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import com.example.manga_ln_app.domain.usecase.FileInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChapterApi @Inject constructor(
    private val client: HttpClient,
    private val credStorage: CredentialsStorage
) {
    private val baseUrl = "http://192.168.95.90:8080"

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

    suspend fun postChapter(info: List<FileInfo>, chapContName: String, chapName: String) {
        try {
            val response = client.submitFormWithBinaryData(
                url = "$baseUrl/chapter",
                formData = formData {
                    append("contentName", chapContName)
                    append("chapterName", chapName)
                    
                    info.forEach { fileInfo ->
                        append("data", fileInfo.bytes, Headers.build {
                            append(HttpHeaders.ContentType, fileInfo.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=${fileInfo.name}")
                        })
                    }
                }
            ) {
                credStorage.getToken().first()?.let { token ->
                    bearerAuth(token)
                }
            }
            
            when(response.status) {
                HttpStatusCode.OK -> {
                    val parsedResponse = response.body<String>()
                    println("POST chapter response: $parsedResponse")
                }
                HttpStatusCode.Forbidden -> throw Exception("Invalid credentials")
                HttpStatusCode.BadRequest -> throw Exception("Bad request")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error when uploading chapter: ${e.message}")
        }
    }

    suspend fun getBetaChapters(): List<ChapterBetaDto>{
        try {
            val response = client.get("$baseUrl/chapter/beta") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    response.body<List<ChapterBetaDto>>()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
            throw e
        }
    }

    suspend fun getBetaComments(): List<CommentBetaDto>{
        try {
            val response = client.get("$baseUrl/comment/beta") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    response.body<List<CommentBetaDto>>()
                }
                HttpStatusCode.Unauthorized -> throw Exception("Unauthorized")
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
            throw e
        }
    }

    suspend fun approveChapter(id: Int) {
        try {
            val response = client.post("$baseUrl/chapter/approve/$id") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
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

    suspend fun approveComment(id: Int) {
        try {
            val response = client.post("$baseUrl/comment/approve/$id") {
                contentType(ContentType.Application.Json)
                credStorage.getToken().first()?.let {
                    bearerAuth(it)
                }
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