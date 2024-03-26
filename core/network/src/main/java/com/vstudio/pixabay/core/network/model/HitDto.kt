package com.vstudio.pixabay.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HitDto(
    val id: Long,
    @SerialName(value = "pageURL")
    val pageUrl: String,
    val type: String,
    val tags: String,
    @SerialName(value = "previewURL")
    val previewUrl: String,
    val previewWidth: Int,
    val previewHeight: Int,
    @SerialName(value = "webformatURL")
    val webFormatUrl: String,
    val webFormatWidth: Int? = null,
    val webFormatHeight: Int? = null,
    @SerialName(value = "largeImageURL")
    val largeImageUrl: String,
    @SerialName(value = "fullHDURL")
    val fullHdUrl: String? = null,
    @SerialName(value = "imageURL")
    val imageUrl: String? = null,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    @SerialName(value = "user_id")
    val userId: Long,
    @SerialName(value = "user")
    val userName: String,
    @SerialName(value = "userImageURL")
    val userImageUrl: String,
)