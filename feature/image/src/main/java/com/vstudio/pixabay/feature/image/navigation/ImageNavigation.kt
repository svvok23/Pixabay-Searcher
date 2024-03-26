package com.vstudio.pixabay.feature.image.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vstudio.pixabay.core.common.ui.ScaleTransitionDirection
import com.vstudio.pixabay.core.common.ui.scaleIntoContainer
import com.vstudio.pixabay.core.common.ui.scaleOutOfContainer
import com.vstudio.pixabay.feature.image.ui.ImageScreen

const val IMAGE_SCREEN_ROUTE = "image_screen"
const val IMAGE_ID_ARG = "image_id"

class ImageArgs(val imageId: Long) {
    constructor(savedStateHandle: SavedStateHandle)
            : this(imageId = checkNotNull(savedStateHandle[IMAGE_ID_ARG]))
}

fun NavGraphBuilder.imageScreen(onBack: () -> Unit) {
    composable(
        route = "$IMAGE_SCREEN_ROUTE/{$IMAGE_ID_ARG}",
        arguments = listOf(
            navArgument(IMAGE_ID_ARG) { type = NavType.IntType },
        ),
        enterTransition = { scaleIntoContainer() },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
        popEnterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
        popExitTransition = { scaleOutOfContainer() }
    ) {
        ImageScreen(onBack = onBack)
    }
}