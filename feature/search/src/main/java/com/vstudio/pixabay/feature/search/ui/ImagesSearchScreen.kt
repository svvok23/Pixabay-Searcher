@file:OptIn(ExperimentalMaterialApi::class)

package com.vstudio.pixabay.feature.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState.Loading
import androidx.paging.compose.collectAsLazyPagingItems
import com.vstudio.pixabay.core.common.ui.component.CollapsingLayout
import com.vstudio.pixabay.core.common.ui.component.CollapsingLayoutStyle
import com.vstudio.pixabay.core.common.ui.component.LineShadow
import com.vstudio.pixabay.core.common.ui.component.SearchBarWithHistory
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme
import com.vstudio.pixabay.feature.search.R
import com.vstudio.pixabay.feature.search.ui.ScreenStyle.Grid
import com.vstudio.pixabay.feature.search.ui.ScreenStyle.List
import com.vstudio.pixabay.core.common.R as common_R

private enum class ScreenStyle {
    List, Grid
}

@Composable
fun ImagesSearchScreen(
    viewModel: ImagesSearchViewModel = hiltViewModel(),
    onImageClick: (imageId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewStyle = rememberSaveable { mutableStateOf(List) }

    val images = viewModel.imagesPagingFlow.collectAsLazyPagingItems()
    val toRefresh by viewModel.toRefresh.collectAsStateWithLifecycle()
    LaunchedEffect(toRefresh) {
        if (toRefresh) images.refresh()
    }

    CollapsingLayout(
        style = CollapsingLayoutStyle.Sticky,
        modifier = Modifier.fillMaxWidth(),
        collapsingTop = {
            SearchToolBar(modifier, viewModel, viewStyle)
        },
        bodyContent = {

            var refreshing by remember { mutableStateOf(false) }
            fun refresh() {
                refreshing = true
                images.refresh()
            }

            val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

            Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
                // TODO to viewModel and State ??
                when {
                    // Error Screen
                    images.loadState.hasError -> {
                        refreshing = false
                        ErrorScreen(
                            retryAction = ::refresh,
                            modifier = modifier.fillMaxSize()
                        )
                    }

                    // Loading Screen
                    images.loadState.refresh is Loading -> {
                        if (images.itemCount > 0) refreshing = true
                        LoadingScreen(modifier = modifier.fillMaxSize())
                    }

                    // Empty Screen
                    images.itemCount <= 0 -> {
                        refreshing = false
                        EmptyScreen(modifier = modifier.fillMaxSize())
                    }

                    // Images Search
                    else -> {
                        refreshing = false
                        when (viewStyle.value) {
                            List -> ImagesList(
                                images = images,
                                onImageClick = onImageClick,
                                modifier = modifier.fillMaxWidth()
                            )

                            Grid -> ImagesGrid(
                                images = images,
                                onImageClick = onImageClick,
                                modifier = modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                PullRefreshIndicator(
                    modifier = Modifier.align(alignment = Alignment.TopCenter),
                    refreshing = refreshing,
                    state = pullRefreshState,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }

            LineShadow(
                modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .align(Alignment.TopCenter)
            )
        }
    )

}

@Composable
private fun SearchToolBar(
    modifier: Modifier,
    viewModel: ImagesSearchViewModel,
    viewStyle: MutableState<ScreenStyle>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {

        var searchIsActive by rememberSaveable { mutableStateOf(false) }

        val icon = when (viewStyle.value) {
            List -> Icons.Filled.Dashboard
            Grid -> Icons.AutoMirrored.Filled.List
        }

        if (!searchIsActive) {
            IconButton(
                onClick = { viewStyle.switch() },
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 10.dp, start = 8.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        SearchBarWithHistory(
            onSearch = {
                viewModel.onSearch(it)
            },
            onQueryChanged = viewModel::onQueryChanged,
            queryFlow = viewModel.queryFlow,
            historyFlow = viewModel.queriesHistoryFlow,
            onActiveChange = {
                searchIsActive = it
            },
            modifier = Modifier.padding(
                bottom = 4.dp,
                end = if (searchIsActive) 0.dp else 8.dp
            )
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = common_R.string.loading),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 200.dp)
        )
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.no_image_found),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 60.dp)
                .padding(bottom = 200.dp)
        )
    }
}

@Composable
private fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = common_R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(common_R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(common_R.string.retry), color = Color.White)
        }
    }
}

private fun MutableState<ScreenStyle>.switch() {
    this.value = if (this.value == List) Grid else List
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    PixabayTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyScreenPreview() {
    PixabayTheme {
        EmptyScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    PixabayTheme {
        ErrorScreen({})
    }
}
