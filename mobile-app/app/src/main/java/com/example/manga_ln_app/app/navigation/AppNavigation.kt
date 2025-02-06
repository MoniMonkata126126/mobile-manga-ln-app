package com.example.manga_ln_app.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.presentation.content.ContentPageRoot
import com.example.manga_ln_app.presentation.home.HomePageRoot
import com.example.manga_ln_app.presentation.login.LoginScreenRoot
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.PageGraph
    ) {
        navigation<Route.PageGraph>(
            startDestination = Route.LoginPage
        )
        {
            composable<Route.LoginPage> {
                LoginScreenRoot(
                    onAuthSelf = { navController.navigate(Route.HomePage) },
                    onError = { navController.navigate(Route.LoginPage) }
                )
            }



            composable<Route.HomePage> {
                HomePageRoot(
                    onContentClick = { content ->
                        navController.navigate(Route.ContentPage)
                    }
                )
            }

            composable<Route.ContentPage> {
//                backStackEntry ->
//                val parentEntry = remember(backStackEntry) {
//                    navController.getBackStackEntry(Route.HomePage)
//                }
//
//                val contentJson = parentEntry.arguments?.getString("content")
//                val content: ListItem.Content? = contentJson?.let {
//                    Json.decodeFromString<ListItem.Content>(it)
//                }

                val content: ListItem.Content? = null

                ContentPageRoot(
                    content = content,
                    onChapterClick = {
                    }
                )
            }
        }
    }
}