package com.example.manga_ln_app.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.UUID
import javax.inject.Inject

class FileReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ioDispatcher = Dispatchers.IO
    
    fun uriToFileInfo(contentUri: Uri): FileInfo {
        val bytes = context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: byteArrayOf()
        val filename = UUID.randomUUID().toString()
        val mimeType = context.contentResolver.getType(contentUri) ?: ""

        return FileInfo(
            name = filename,
            mimeType = mimeType,
            bytes = bytes
        )

    }
}


class FileInfo(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray
)