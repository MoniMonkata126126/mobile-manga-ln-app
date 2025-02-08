package com.example.manga_ln_app.di

import android.util.Log
import com.example.manga_ln_app.data.remote.AuthApi
import com.example.manga_ln_app.data.remote.ContentApi
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HTTP Client", message)
                    }
                }
                level = LogLevel.ALL
            }
            
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: HttpClient): AuthApi {
        return AuthApi(client)
    }

    @Provides
    @Singleton
    fun provideContentApi(client: HttpClient, credStorage: CredentialsStorage): ContentApi {
        return ContentApi(client, credStorage)
    }
} 