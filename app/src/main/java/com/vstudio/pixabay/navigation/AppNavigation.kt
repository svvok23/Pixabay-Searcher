package com.vstudio.pixabay.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.vstudio.pixabay.feature.image.navigation.IMAGE_SCREEN_ROUTE
import com.vstudio.pixabay.feature.search.navigation.SEARCH_ROUTE

fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) =
    navigate(SEARCH_ROUTE, navOptions)

fun NavController.navigateToImageScreen(photoId: Long, navOptions: NavOptions? = null) =
    navigate("$IMAGE_SCREEN_ROUTE/$photoId", navOptions)