package com.example.manga_ln_app.presentation.content.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.manga_ln_app.domain.model.ListItem

@Composable
fun ChapterList(
    listItems: List<ListItem.Chapter>,
    onBookClick: (ListItem.Chapter) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(
            items = listItems,
            key = { it.id }
        ) { listItem ->
            ChapterListItem(
                item = listItem,
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth(),
                onClick = {
                    onBookClick(listItem)
                }
            )
        }
    }
}