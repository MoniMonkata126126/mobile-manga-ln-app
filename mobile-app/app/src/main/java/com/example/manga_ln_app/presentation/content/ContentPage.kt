package com.example.manga_ln_app.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.presentation.content.components.ChapterList

@Composable
fun ContentPageRoot(
    viewModel: ContentPageViewModel = hiltViewModel(),
    onChapterClick: (ListItem) -> Unit,
    onBackClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()

    ContentPage(
        state = state,
        content = content,
        onAction = { action ->
            when(action) {
                is ContentPageAction.OnChapterClick -> onChapterClick(action.chapter)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        onBackClick = onBackClick
    )
}


@Composable
fun ContentPage(
    state: ContentPageState,
    content: ListItem.Content?,
    onAction: (ContentPageAction) -> Unit,
    onBackClick: () -> Unit
) {

    val chaptersListState = rememberLazyListState()

    Box{

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = Color.hsv(275f, 0.43f, 0.86f),
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            ) {
                Box{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    ) {
                        content?.let {
                            Text(
                                text = content.title,
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "by ${content.author}",
                                modifier = Modifier
                                    .padding( top = 10.dp, bottom = 22.dp),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.isLoading) {
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
                                    state.chapters.isEmpty() -> {
                                        Text(
                                            text = "No search results",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    else -> {
                                        ChapterList(
                                            listItems = state.chapters,
                                            onBookClick = {
                                                onAction(ContentPageAction.OnChapterClick(it))
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.hsv(275f, 0.25f, 0.91f)),
                                            scrollState = chaptersListState
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(top = 6.dp, start = 16.dp)
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "go back",
                tint = Color.Black
            )
        }
    }
}