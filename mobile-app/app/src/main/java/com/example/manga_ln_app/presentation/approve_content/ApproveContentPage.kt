package com.example.manga_ln_app.presentation.approve_content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.manga_ln_app.presentation.approve_content.components.ChaptersBox
import com.example.manga_ln_app.presentation.approve_content.components.CommentsBox
import com.example.manga_ln_app.presentation.approve_content.components.ContentsBox


@Composable
fun ApproveContentPageRoot(
    viewModel: ApprovePageViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ApproveContentPage(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onBackClick = onBackClick
    )

}

@Composable
fun ApproveContentPage(
    state: ApprovePageState,
    onAction: (ApprovePageAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = rememberLazyListState()
        ) {

            item {
                Text(
                    text = "Content to be approved",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(bottom = 14.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                )
            }

            items(
                items = state.contentsBeta,
                key = { it.id.toString() + "a" }
            ) { contentBeta ->
                ContentsBox(
                    contentType = contentBeta.contentType,
                    name = contentBeta.name,
                    username = contentBeta.authorUsername,
                    onApprove = { onAction(ApprovePageAction.OnApproveContent(contentBeta.id)) }
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
                Text(
                    text = "Chapters to be approved",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(bottom = 14.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }

            items(
                items = state.chaptersBeta,
                key = { it.id.toString() + "b" }
            ) { chapter ->
                ChaptersBox(
                    name = chapter.name,
                    contentName = chapter.contentName,
                    onApprove = { onAction(ApprovePageAction.OnApproveChapter(chapter.id)) }
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
                Text(
                    text = "Comments to be approved",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(bottom = 14.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }

            items(
                items = state.commentsBeta,
                key = { it.id.toString() + "c" }
            ) { comment ->
                CommentsBox(
                    author = comment.author,
                    text = comment.text,
                    onApprove = { onAction(ApprovePageAction.OnApproveComment(comment.id)) }
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }
        }
    }
    Box {
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