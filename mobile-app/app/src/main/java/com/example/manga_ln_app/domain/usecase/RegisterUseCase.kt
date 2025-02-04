package com.example.manga_ln_app.domain.usecase

import com.example.manga_ln_app.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        return repository.register(username, password)
    }
} 