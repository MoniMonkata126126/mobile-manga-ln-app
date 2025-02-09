package com.example.manga_ln_app.presentation.post_content

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.manga_ln_app.presentation.post_content.components.ChapterUploadField
import com.example.manga_ln_app.presentation.post_content.components.ContentUploadField

@Composable
fun PostPageRoot(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { contentUris ->
        contentUris.let {
            viewModel.onAction(PostPageAction.OnChangeSelectedUris(contentUris))
        }
    }

    PostPage(
        state = state,
        onBackClick = onBackClick,
        filePickerLauncher = filePickerLauncher,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun PostPage(
    state: PostPageState,
    onBackClick: () -> Unit,
    filePickerLauncher: ManagedActivityResultLauncher<String, List<Uri>>,
    onAction: (PostPageAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current



    Box {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .padding(vertical = 12.dp)
                .padding(top = 20.dp)
                .padding(bottom = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .padding(horizontal = 22.dp),
                text = "Upload content bellow:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(
                modifier = Modifier.height(2.dp)
            )
            ContentUploadField(
                contentName = state.contentName,
                onNameChange = { onAction(PostPageAction.OnContentNameChange(it)) },
                onChangeDropdownSelect = { onAction(PostPageAction.OnSelectedDropdownOneChange(it)) },
                onButtonClick = { onAction(PostPageAction.OnPostButtonOneClick) },
                onImeDone = {
                    keyboardController?.hide()
                }
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 22.dp),
                text = "Upload chapter bellow:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(
                modifier = Modifier.height(2.dp)
            )

            ChapterUploadField(
                contentName = state.chapContName,
                chapterName = state.chapName,
                isUploading = state.isUploading,
                filePickerLauncher = filePickerLauncher,
                onChapNameChange = { onAction(PostPageAction.OnChapterNameChange(it)) },
                onContNameChange = { onAction(PostPageAction.OnChapContentNameChange(it)) },
                onChangeDropdownSelect = { onAction(PostPageAction.OnSelectedDropdownTwoChange(it)) },
                onButtonClick = { onAction(PostPageAction.OnPostButtonTwoClick) },
                onImeDone = {
                    keyboardController?.hide()
                }
            )
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