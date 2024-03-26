package com.vstudio.pixabay.feature.image.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.usecase.GetCashedImageUrlUseCase
import com.vstudio.pixabay.core.domain.usecase.GetImageUseCase
import com.vstudio.pixabay.feature.image.navigation.ImageArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    getCashedImageUseCase: GetCashedImageUrlUseCase,
    getImageUseCase: GetImageUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val imageArgs: ImageArgs = ImageArgs(savedStateHandle)

    private val _imageFlow = MutableStateFlow(Pair<Image?, String?>(null, null))
    val imageFlow: StateFlow<Pair<Image?, String?>> = _imageFlow.asStateFlow()

    init {
        getImageUseCase(imageArgs.imageId).onEach {
            _imageFlow.value = Pair(it, getCashedImageUseCase(it))
        }.launchIn(viewModelScope)
    }

}