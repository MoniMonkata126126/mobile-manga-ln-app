package com.example.manga_ln_app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.usecase.LoginUseCase
import com.example.manga_ln_app.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }
            is LoginAction.OnUsernameChange -> {
                _state.update { it.copy(username = action.username) }
            }
            LoginAction.OnLogin -> {
                login()
            }
            LoginAction.OnRegister -> {
                register()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = loginUseCase(state.value.username, state.value.password)
                result.onSuccess { role ->
                    _state.update { it.copy(isLoading = false, loggedInRole = role, isLoggedIn = true ) }
                }.onFailure { error ->
                    println("Login failure: ${error.message}")
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                println("Login exception: ${e.message}")
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(state.value.username, state.value.password)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isRegistered = true, isLoggedIn = true ) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
} 