package com.vstudio.pixabay.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vstudio.pixabay.feature.search.ui.ImagesSearchScreen

const val SEARCH_ROUTE = "search"

fun NavGraphBuilder.searchScreen(onImageClick: (imageId: Long) -> Unit) {
    composable(route = SEARCH_ROUTE) {
        ImagesSearchScreen(onImageClick = onImageClick)
    }
}