package com.example.manga_ln_app.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object PageGraph: Route

    @Serializable
    data object LoginPage: Route

    @Serializable
    data object HomePage: Route
} 