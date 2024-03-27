package com.vstudio.pixabay.core.data.mapper

import com.vstudio.pixabay.core.common.Mapper
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.UserEntity
import com.vstudio.pixabay.core.network.model.HitDto
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.model.User
import javax.inject.Inject

internal class ImageMapperDtoToModel @Inject constructor() : Mapper<HitDto, Image> {
    override fun mapFrom(from: HitDto): Image {
        val user = User(
            id = from.userId,
            name = from.userName,
            avatarUrl = from.userImageUrl
        )
        return Image(
            dbId = 0,
            id = from.id,
            width = from.imageWidth,
            height = from.imageHeight,
            pageUrl = from.pageUrl,
            previewUrl = from.previewUrl,
            webFormatUrl = from.webFormatUrl,
            largeImageUrl = from.largeImageUrl,
            fullHdUrl = from.fullHdUrl,
            originalUrl = from.imageUrl,
            tags = from.tags.tagsToList(),
            user = user,
            views = from.views,
            downloads = from.downloads,
            likes = from.likes,
            comments = from.comments,
        )
    }
}

internal class ImageMapperDtoToEntity @Inject constructor() : Mapper<HitDto, ImageEntity> {
    override fun mapFrom(from: HitDto): ImageEntity {
        val user = UserEntity(
            id = from.userId,
            name = from.userName,
            avatarUrl = from.userImageUrl
        )
        return ImageEntity(
            imageId = from.id,
            width = from.imageWidth,
            height = from.imageHeight,
            pageUrl = from.pageUrl,
            previewUrl = from.previewUrl,
            webFormatUrl = from.webFormatUrl,
            largeImageUrl = from.largeImageUrl,
            fullHdUrl = from.fullHdUrl,
            originalUrl = from.imageUrl,
            tags = from.tags.tagsToList(),
            user = user,
            views = from.views,
            downloads = from.downloads,
            likes = from.likes,
            comments = from.comments,
            searchQuery = ""
        )
    }
}

internal class ImageMapperEntityToModel @Inject constructor() : Mapper<ImageEntity, Image> {
    override fun mapFrom(from: ImageEntity): Image {
        val user = User(
            id = from.user.id,
            name = from.user.name,
            avatarUrl = from.user.avatarUrl
        )
        return Image(
            dbId = from.id,
            id = from.imageId,
            width = from.width,
            height = from.height,
            pageUrl = from.pageUrl,
            previewUrl = from.previewUrl,
            webFormatUrl = from.webFormatUrl,
            largeImageUrl = from.largeImageUrl,
            fullHdUrl = from.fullHdUrl,
            originalUrl = from.originalUrl,
            tags = from.tags,
            user = user,
            views = from.views,
            downloads = from.downloads,
            likes = from.likes,
            comments = from.comments,
        )
    }
}

private fun String.tagsToList() : List<String> {
    val delimiter = ", "
    return this.split(delimiter).map { it.trim() }
}