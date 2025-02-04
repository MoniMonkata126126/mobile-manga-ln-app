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

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _state.update { it.copy(username = event.username) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            LoginEvent.LoginClicked -> {
                login()
            }
            LoginEvent.RegisterClicked -> {
                register()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(state.value.username, state.value.password)
            _state.update { it.copy(isLoading = false) }
            result.onSuccess {
                _isLoggedIn.value = true
            }.onFailure { error ->
                _state.update { it.copy(error = error.message) }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(state.value.username, state.value.password)
            _state.update { it.copy(isLoading = false) }
            result.onSuccess {
                _isLoggedIn.value = true
            }.onFailure { error ->
                _state.update { it.copy(error = error.message) }
            }
        }
    }
} 