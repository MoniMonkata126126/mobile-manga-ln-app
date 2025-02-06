package com.example.manga_ln_app.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.presentation.content.ContentPageRoot
import com.example.manga_ln_app.presentation.home.HomePageRoot
import com.example.manga_ln_app.presentation.login.LoginScreenRoot

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
                    onContentClick = {
                        navController.navigate(Route.ContentPage)
                    }
                )
            }

            composable<Route.ContentPage> {

                ContentPageRoot(
                    onChapterClick = {

                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}