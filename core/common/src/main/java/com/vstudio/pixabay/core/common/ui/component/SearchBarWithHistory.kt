package com.vstudio.pixabay.core.common.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vstudio.pixabay.core.common.R
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithHistory(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    queryFlow: StateFlow<String>,
    historyFlow: StateFlow<List<String>>,
    onActiveChange: (Boolean) -> Unit,
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val query = queryFlow.collectAsState().value
    val historyList = historyFlow.collectAsState()

    SearchBar(
        query = query,
        modifier = modifier,
        onQueryChange = onQueryChanged,
        onSearch = {
            onSearch(query)
            active = false
            onActiveChange(false)
        },
        active = active,
        onActiveChange = {
            active = it
            onActiveChange(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search),
            )
        },
        trailingIcon = {
            if (active || query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        if (query.isNotEmpty()) {
                            onQueryChanged("")
                        } else {
                            onSearch(query)
                            active = false
                            onActiveChange(false)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = if (query.isNotEmpty())
                            stringResource(id = R.string.clear)
                        else
                            stringResource(id = R.string.close),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
    ) {
        LazyColumn(content = {
            historyList.value.forEach {
                item {
                    HistoryItem(query = it) {
                        onQueryChanged(it)
                        onSearch(it)
                        active = false
                        onActiveChange(false)
                    }
                }
            }
        })
    }
}

@Composable
fun HistoryItem(
    query: String,
    onQuerySelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onQuerySelected(query)
            }
            .padding(all = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = stringResource(id = R.string.history),
            modifier = Modifier.padding(end = 8.dp),
        )

        Text(text = query)
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryItemPreview() {
    PixabayTheme {
        HistoryItem("item") {}
    }
}