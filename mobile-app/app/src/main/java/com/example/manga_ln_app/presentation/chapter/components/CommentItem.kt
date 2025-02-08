package com.example.manga_ln_app.presentation.chapter.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manga_ln_app.domain.model.Comment
import kotlin.math.min

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Surface(
        modifier = modifier
            .width(400.dp)
            .padding(horizontal = 12.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = shape
            ),
        shape = shape,
        color = Color.DarkGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = comment.author,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            
            Text(
                text = comment.text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}