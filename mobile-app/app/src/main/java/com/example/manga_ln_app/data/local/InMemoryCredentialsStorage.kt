package com.example.manga_ln_app.data.local

import com.example.manga_ln_app.domain.repository.CredentialsStorage
import com.example.manga_ln_app.domain.repository.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryCredentialsStorage @Inject constructor() : CredentialsStorage {
    private val _token = MutableStateFlow<String?>(null)
    override val token = _token.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    override val username = _username.asStateFlow()

    private val _role = MutableStateFlow<Role?>(null)
    override val role = _role.asStateFlow()


    override fun saveToken(token: String) {
        println("Saving token: $token")
        _token.value = token
    }

    override fun getToken(): Flow<String?> {
        return token
    }

    override fun saveUsername(username: String) {
        println("Saving username: $username")
        _username.value = username
    }

    override fun getUsername(): Flow<String?> {
        return username
    }

    override fun saveRole(role: Role) {
        println("Saving role: $role")
        _role.value = role
    }

    override fun getRole(): Flow<Role?> {
        return role
    }
}