package com.example.manga_ln_app.presentation.home

import androidx.lifecycle.ViewModel
import com.example.manga_ln_app.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    val token = tokenManager.getToken()
} 