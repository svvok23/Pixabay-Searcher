package com.vstudio.pixabay.core.data.mapper

import com.vstudio.pixabay.core.common.Mapper
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.UserEntity
import com.vstudio.pixabay.core.network.model.HitDto
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.model.ImageSize
import com.vstudio.pixabay.core.domain.model.MultiSizeImage
import com.vstudio.pixabay.core.domain.model.User
import com.vstudio.pixabay.core.domain.model.getImageSize
import com.vstudio.pixabay.core.network.NetworkConst.FULL_HD_IMAGE_MAX_SIZE
import com.vstudio.pixabay.core.network.NetworkConst.LARGE_IMAGE_MAX_SIZE
import javax.inject.Inject

internal class ImageMapperDtoToModel @Inject constructor() : Mapper<HitDto, Image> {
    override fun mapFrom(from: HitDto): Image {
        val user = User(
            id = from.userId,
            name = from.userName,
            avatarUrl = from.userImageUrl
        )

        val sizes = mutableMapOf(
            ImageSize(from.previewWidth, from.previewHeight) to from.previewUrl,
            ImageSize(from.webFormatWidth, from.webFormatHeight) to from.webFormatUrl,
        )
        val originalSize = ImageSize(from.imageWidth, from.imageHeight)
        val ratio = originalSize.aspectRatio

        from.largeImageUrl.let { sizes[getImageSize(ratio, LARGE_IMAGE_MAX_SIZE)] = it }
        from.fullHdUrl?.let { sizes[getImageSize(ratio, FULL_HD_IMAGE_MAX_SIZE)] = it }
        from.imageUrl?.let { sizes[originalSize] = it }

        val multiSizeImage = MultiSizeImage(sizes.toMap())

        return Image(
            dbId = 0,
            id = from.id,
            width = from.imageWidth,
            height = from.imageHeight,
            pageUrl = from.pageUrl,
            multiSizeImage = multiSizeImage,
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
            previewWidth = from.previewWidth,
            previewHeight = from.previewHeight,
            webFormatUrl = from.webFormatUrl,
            webFormatWidth = from.webFormatWidth,
            webFormatHeight = from.webFormatHeight,
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

        val originalSize = ImageSize(from.width, from.height)
        val ratio = originalSize.aspectRatio

        val sizes = mutableMapOf(
            ImageSize(from.previewWidth, from.previewHeight) to from.previewUrl,
            ImageSize(from.webFormatWidth, from.webFormatHeight) to from.webFormatUrl,
        )

        from.largeImageUrl.let { sizes[getImageSize(ratio, LARGE_IMAGE_MAX_SIZE)] = it }
        from.fullHdUrl?.let { sizes[getImageSize(ratio, FULL_HD_IMAGE_MAX_SIZE)] = it }
        from.originalUrl?.let { sizes[originalSize] = it }

        val multiSizeImage = MultiSizeImage(sizes.toMap())

        return Image(
            dbId = from.id,
            id = from.imageId,
            width = from.width,
            height = from.height,
            pageUrl = from.pageUrl,
            multiSizeImage = multiSizeImage,
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