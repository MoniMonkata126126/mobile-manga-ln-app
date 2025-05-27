package com.example.manga_ln_app.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val loggedInRole: String? = null
) 