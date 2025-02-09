package com.example.manga_ln_app.domain.usecase

import com.example.manga_ln_app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<String> {
        return repository.login(username, password)
    }
} 