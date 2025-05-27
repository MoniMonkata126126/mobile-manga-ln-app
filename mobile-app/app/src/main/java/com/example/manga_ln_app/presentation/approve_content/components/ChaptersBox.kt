package com.example.manga_ln_app.presentation.approve_content.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChaptersBox(
    name: String,
    contentName: String,
    onApprove: () -> Unit,
) {

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Name: $name",
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.White,
                fontSize = 16.sp
            )

            Text(
                text = "Content name: $contentName",
                modifier = Modifier.padding(vertical = 4.dp),
                color = Color.White,
                fontSize = 16.sp
            )

            Button(
                onClick = onApprove,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(bottom = 8.dp)
                    .widthIn(min = 20.dp)
            ) {
                Text(
                    text = "Approve"
                )
            }
        }
    }
}