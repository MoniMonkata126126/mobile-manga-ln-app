package com.example.manga_ln_app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.manga_ln_app.presentation.home.components.ContentList
import com.example.manga_ln_app.presentation.home.components.SearchBar

@Composable
fun HomePageRoot(
    role: String,
    viewModel: HomePageViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onClickMod: () -> Unit,
    onClickAuthor: () -> Unit,
    onContentClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomePage(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
            when(action) {
                HomePageAction.OnLogout -> onLogout()
                is HomePageAction.OnContentClick -> onContentClick()
                else -> Unit
            }
        },
        onClickMod = onClickMod,
        onClickAuthor = onClickAuthor

    )
}


@Composable
fun HomePage(
    state: HomePageState,
    onAction: (HomePageAction) -> Unit,
    onClickMod: () -> Unit,
    onClickAuthor: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultsMListState = rememberLazyListState()
    val searchResultsLNListState = rememberLazyListState()

    LaunchedEffect(state.searchResultsM) {
        searchResultsMListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(HomePageAction.OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsv(240F, 0.66F, 0.94F))
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(HomePageAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            color = Color.hsv(275f, 0.43f, 0.86f),
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[state.selectedTabIndex])
                        )
                    }
                ) {
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            onAction(HomePageAction.OnTabSelected(0))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = Color.hsv(275f, 0.43f, 0.86f),
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Comics",
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            onAction(HomePageAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = Color.hsv(275f, 0.43f, 0.86f),
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Light Novels",
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { pageIndex ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(pageIndex) {
                            0 -> {
                                if(state.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.errorMessage != null -> {
                                            Text(
                                                text = state.errorMessage,
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        state.searchResultsM.isEmpty() -> {
                                            Text(
                                                text = "No search results",
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        else -> {
                                            ContentList(
                                                listItems = state.searchResultsM,
                                                onContentClick = {
                                                    onAction(HomePageAction.OnContentClick(it))
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                scrollState = searchResultsMListState
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {
                                if( state.isLoading ){
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.errorMessage != null -> {
                                            Text(
                                                text = state.errorMessage,
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        state.searchResultsLN.isEmpty() -> {
                                            Text(
                                                text = "No search results",
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        else -> {
                                            ContentList(
                                                listItems = state.searchResultsLN,
                                                onContentClick = {
                                                    onAction(HomePageAction.OnContentClick(it))
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                scrollState = searchResultsLNListState
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .wrapContentHeight()
                        .padding(bottom = 20.dp, top = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Black,
                        disabledContentColor = Color.White,
                        containerColor = Color.Black
                    )

                    Button(
                        onClick = { onAction(HomePageAction.OnLogout) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp),
                        colors = colors
                    ) {
                        Text(
                            text = "Log out",
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if(state.userRole.name != "READER" && state.userRole.name != "ADMIN"){
                        Spacer(
                            modifier = Modifier.width(22.dp)
                        )
                        when(state.userRole.name){
                            "AUTHOR" -> {
                                Button(
                                    onClick = { onClickAuthor() },
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = colors
                                ) {
                                    Text(
                                        text = "Post content",
                                        textAlign = TextAlign.Center,
                                        textDecoration = TextDecoration.Underline,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            "MODERATOR" -> {
                                Button(
                                    onClick = { onClickMod() },
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = colors
                                ) {
                                    Text(
                                        text = "Approve content",
                                        textAlign = TextAlign.Center,
                                        textDecoration = TextDecoration.Underline,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}