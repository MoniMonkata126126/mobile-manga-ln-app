package com.example.manga_ln_app.presentation.chapter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.manga_ln_app.presentation.chapter.components.CommentItem
import com.example.manga_ln_app.presentation.chapter.components.TypeComment

@Composable
fun ChapterPageRoot(
    id: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ChapterPageViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChapterPage(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun ChapterPage(
    state: ChapterPageState,
    onAction: (ChapterPageAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.hsv(275f, 0.43f, 0.86f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = state.currentChapter?.name ?: "",
                modifier = Modifier
                    .padding(top = 20.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = rememberLazyListState()
            ) {
                state.currentChapter?.let { chapter ->
                    items(
                        items = chapter.chapterImagesURLs,
                        key = { url -> url }
                    ) { imageUrl ->


                        val imageLoadResult by remember {
                            mutableStateOf<Result<Painter>?>(null)
                        }

                        val painter = rememberAsyncImagePainter(
                            //model = imageUrl.replace(" ", "%20"),
                            model = "https://covers.openlibrary.org/b/olid/OL34780722M-L.jpg",
                            onSuccess = {
                                Result.success(it.painter)
                            },
                            onError = {
                                println("Image painter error: " + it.painter)
                                println("Image error message: " + it.result.throwable.message)
                                it.result.throwable.printStackTrace()
                            },
                            onLoading = {
                                println("Image is loading: " + it.painter.toString())
                            }
                        )

                        when (imageLoadResult) {
                            null -> CircularProgressIndicator(
                                modifier = Modifier.size(60.dp)
                            )
                            else -> {
                                Image(
                                    painter = painter,
                                    contentDescription = chapter.name,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                item {
                    TypeComment(
                        commentText = state.currentComment,
                        onPost = { onAction(ChapterPageAction.OnCommentPost) },
                        onImeDone = { keyboardController?.hide() },
                        onCommentChange = { onAction(ChapterPageAction.OnCommentChange(it)) }
                    )
                }

                items(
                    items = state.currentChapter?.comments ?: emptyList(),
                    key = { comment -> comment.id }
                ) { comment ->
                    CommentItem(
                        comment = comment,
                        modifier = Modifier
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
}

