package com.example.manga_ln_app.presentation.login

sealed class LoginEvent {
    data class UsernameChanged(val username: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object LoginClicked : LoginEvent()
    data object RegisterClicked : LoginEvent()
} 