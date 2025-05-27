package com.example.manga_ln_app.presentation.post_content

import android.net.Uri
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type

data class PostPageState(
    val contentName: String = "",
    val contentType: Type = Type.MANGA,
    val chapContOptions: List<ListItem.Content> = emptyList(),
    val chapContName: String = "",
    val chapContType: Type = Type.MANGA,
    val chapName: String = "",
    val isUploading: Boolean = false,
    val selectedUris: List<Uri> = emptyList()
)
