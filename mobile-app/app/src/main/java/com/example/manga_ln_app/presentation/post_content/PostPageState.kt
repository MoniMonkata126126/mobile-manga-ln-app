package com.example.manga_ln_app.presentation.post_content

import android.net.Uri
import com.example.manga_ln_app.domain.model.Type

data class PostPageState(
    val contentName: String = "",
    val contentType: Type = Type.MANGA,
    val chapContName: String = "",
    val chapContType: Type = Type.MANGA,
    val chapName: String = "",
    val isUploading: Boolean = false,
    val selectedUris: List<Uri> = emptyList()
)
