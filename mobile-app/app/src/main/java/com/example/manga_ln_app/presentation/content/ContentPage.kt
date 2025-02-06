package com.example.manga_ln_app.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.presentation.content.components.ChapterList

@Composable
fun ContentPageRoot(
    viewModel: ContentPageViewModel = hiltViewModel(),
    content: ListItem.Content?,
    onChapterClick: (ListItem) -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ContentPage(
        state = state,
        content = content,
        onAction = { action ->
            when(action) {
                is ContentPageAction.OnChapterClick -> onChapterClick(action.chapter)
                else -> Unit
            }
            viewModel.onAction(action)
        }

    )
}


@Composable
fun ContentPage(
    state: ContentPageState,
    content: ListItem.Content?,
    onAction: (ContentPageAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                content?.let {
                    Text(
                        text = content.title,
                        modifier = Modifier
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
                            state.searchResults.isEmpty() -> {
                                Text(
                                    text = "No search results",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            else -> {
                                ChapterList(
                                    listItems = state.searchResults,
                                    onBookClick = {
                                        onAction(ContentPageAction.OnChapterClick(it))
                                                  },
                                    modifier = Modifier.fillMaxSize(),
                                    //scrollState = searchResultsMListState
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}