package com.example.manga_ln_app.presentation.login

sealed interface LoginAction {
    data class OnUsernameChange(val username: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnLogin : LoginAction
    data object OnRegister : LoginAction
} 