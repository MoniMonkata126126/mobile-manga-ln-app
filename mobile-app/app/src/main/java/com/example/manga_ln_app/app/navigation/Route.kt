package com.example.manga_ln_app.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object PageGraph: Route

    @Serializable
    data object LoginPage: Route

    @Serializable
    data class HomePage( val role: String ): Route

    @Serializable
    data object ContentPage: Route

    @Serializable
    data class ChapterPage( val id: Int, val name: String ): Route

    @Serializable
    data object PostPage: Route

    @Serializable
    data object ApprovePage: Route
} 