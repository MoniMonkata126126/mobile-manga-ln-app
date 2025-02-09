package com.example.manga_ln_app.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.manga_ln_app.app.navigation.AppNavigation
import com.example.manga_ln_app.ui.theme.MangalnappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangalnappTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
} 