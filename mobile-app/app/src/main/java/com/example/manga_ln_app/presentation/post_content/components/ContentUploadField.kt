package com.example.manga_ln_app.presentation.post_content.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.manga_ln_app.domain.model.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentUploadField(
    contentName: String,
    onNameChange: (String) -> Unit,
    onChangeDropdownSelect: (Type) -> Unit,
    onButtonClick: () -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember{
        mutableStateOf(false)
    }

    val list = listOf(Type.MANGA, Type.LN)

    var dropdownText by remember{
        mutableStateOf(list[0])
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(vertical = 20.dp)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = contentName,
            onValueChange = onNameChange,
            shape = RoundedCornerShape(100),
            placeholder = {
                Text(
                    text = "Content name..."
                )
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeDone()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(100),
                    color = Color.White
                )
                .padding(vertical = 8.dp)
                .minimumInteractiveComponentSize()
        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp),
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },

            ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = dropdownText.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
            )

            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                list.forEachIndexed { index, value ->
                    DropdownMenuItem(
                        text = { Text(text = value.name) },
                        onClick = {
                            dropdownText = list[index]
                            onChangeDropdownSelect(dropdownText)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
                .padding(vertical = 10.dp),
            colors = ButtonColors(
                contentColor = Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.White,
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Post"
            )
        }
    }
}