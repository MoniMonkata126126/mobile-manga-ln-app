package com.example.manga_ln_app.presentation.post_content.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterUploadField(
    contentList: List<ListItem.Content>,
    chapterName: String,
    isUploading: Boolean,
    filePickerLauncher: ManagedActivityResultLauncher<String, List<Uri>>,
    selectedUris: List<Uri>,
    onChapNameChange: (String) -> Unit,
    onChangeDropdownSelect: (ListItem.Content) -> Unit,
    onButtonClick: () -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember{
        mutableStateOf(false)
    }

    if (contentList.isNotEmpty()) {

        var dropdownItem by remember{
            mutableStateOf(contentList[0])
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp),
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },

                ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = dropdownItem.title,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    contentList.forEachIndexed { index, value ->
                        DropdownMenuItem(
                            text = { Text(text = value.title) },
                            onClick = {
                                dropdownItem = contentList[index]
                                onChangeDropdownSelect(dropdownItem)
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            OutlinedTextField(
                value = chapterName,
                onValueChange = onChapNameChange,
                shape = RoundedCornerShape(100),
                placeholder = {
                    Text(
                        text = "Chapter name..."
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

            if (!isUploading) {
                Row {
                    Button(onClick = {
                        if (dropdownItem.type == Type.MANGA) {
                            filePickerLauncher.launch("image/jpeg")
                        } else if (dropdownItem.type == Type.LN) {
                            filePickerLauncher.launch("text/plain")
                        }
                    }) {
                        Text(text = "Pick files")
                    }

                    if(selectedUris.isNotEmpty()){
                        Text(
                            text = "Selected files: ${selectedUris.size}",
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 8.dp)
                                .padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
                    .padding(vertical = 8.dp),
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
}