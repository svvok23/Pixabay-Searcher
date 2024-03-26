package com.vstudio.pixabay.core.network.model

import kotlinx.serialization.SerialName

enum class ImageType {
    @SerialName("all")
    ALL,
    @SerialName("photo")
    PHOTO,
    @SerialName("illustration")
    ILLUSTRATION,
    @SerialName("vector")
    VECTOR
}