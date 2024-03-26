package com.vstudio.pixabay.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vstudio.pixabay.MainActivityViewModel
import com.vstudio.pixabay.feature.image.navigation.imageScreen
import com.vstudio.pixabay.feature.search.navigation.SEARCH_ROUTE
import com.vstudio.pixabay.feature.search.navigation.searchScreen
import com.vstudio.pixabay.navigation.navigateToImageScreen
import com.vstudio.pixabay.core.common.R as common_R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PixabayApp(
    viewModel: MainActivityViewModel,
) {

    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val notConnectedMessage = stringResource(common_R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {

        val navController: NavHostController = rememberNavController()
        val imageIdShowDialog = remember { mutableStateOf<Long?>(null) }

        NavHost(
            navController = navController,
            startDestination = SEARCH_ROUTE
        ) {
            searchScreen { id ->
                imageIdShowDialog.value = id
            }

            imageScreen { navController.navigateUp() }
        }

        imageIdShowDialog.value?.let { imageId ->
            ShowDetailsDialog(
                onDismissRequest = { imageIdShowDialog.value = null },
                onConfirmation = {
                    if (!isOffline) navController.navigateToImageScreen(imageId)
                    imageIdShowDialog.value = null
                }
            )
        }
    }
}