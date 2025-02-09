package com.example.manga_ln_app.presentation.approve_content.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manga_ln_app.domain.model.Type
import io.ktor.http.ContentType

@Composable
fun ContentsBox(
    contentType: Type,
    name: String,
    username: String,
    onApprove: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Column{

                Text(
                    text = "Name: $name",
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Type: ${contentType.name}",
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Author: $username",
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }


            Button(
                onClick = onApprove
            ) {
                Text(
                    text = "Approve"
                )
            }
        }
    }
}