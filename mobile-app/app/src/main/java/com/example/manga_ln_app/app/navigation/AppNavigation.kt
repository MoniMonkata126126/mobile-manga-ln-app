package com.example.manga_ln_app.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.manga_ln_app.presentation.approve_content.ApproveContentPageRoot
import com.example.manga_ln_app.presentation.chapter.ChapterPageRoot
import com.example.manga_ln_app.presentation.content.ContentPageRoot
import com.example.manga_ln_app.presentation.home.HomePageRoot
import com.example.manga_ln_app.presentation.login.LoginScreenRoot
import com.example.manga_ln_app.presentation.post_content.PostPageRoot

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
                    onAuthSelf = { role -> navController.navigate(Route.HomePage(role)) },
                    onError = { navController.navigate(Route.LoginPage) }
                )
            }

            composable<Route.HomePage> { entry ->
                val args = entry.toRoute<Route.HomePage>()

                HomePageRoot(
                    role = args.role,
                    onLogout = {
                        navController.navigate(Route.LoginPage)
                    },
                    onContentClick = {
                        navController.navigate(Route.ContentPage)
                    },
                    onClickAuthor = {
                        navController.navigate(Route.PostPage)
                    },
                    onClickMod = {
                        navController.navigate(Route.ApprovePage)
                    }
                )
            }

            composable<Route.ContentPage> {

                ContentPageRoot(
                    onChapterClick = {
                        chapter ->
                        navController.navigate(Route.ChapterPage(chapter.id, chapter.name))
                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable<Route.ChapterPage> { entry ->
                val args = entry.toRoute<Route.ChapterPage>()

                ChapterPageRoot(
                    id = args.id,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable<Route.PostPage> {
                PostPageRoot(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable<Route.ApprovePage> {
                ApproveContentPageRoot(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}