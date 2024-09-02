package com.vstudio.pixabay.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(
    tableName = "images_table",
    indices = [
        Index(value = ["imageId"], unique = true),
        Index(value = ["searchQuery"], unique = false),
    ]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageId: Long,
    val width: Int,
    val height: Int,
    var pageUrl: String,
    val previewUrl: String,
    @ColumnInfo(defaultValue = 0.toString())
    val previewWidth: Int,
    @ColumnInfo(defaultValue = 0.toString())
    val previewHeight: Int,
    val webFormatUrl: String,
    @ColumnInfo(defaultValue = 0.toString())
    val webFormatWidth: Int,
    @ColumnInfo(defaultValue = 0.toString())
    val webFormatHeight: Int,
    val largeImageUrl: String,
    val fullHdUrl: String?,
    val originalUrl: String?,
    val tags: List<String>,
    @Embedded(prefix = "user")
    val user: UserEntity,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    var searchQuery: String?
)

data class UserEntity(
    val id: Long,
    val name: String,
    val avatarUrl: String,
)