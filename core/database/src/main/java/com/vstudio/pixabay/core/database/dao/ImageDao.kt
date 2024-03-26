package com.vstudio.pixabay.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vstudio.pixabay.core.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>): List<Long>

    @Upsert
    suspend fun upsertAll(images: List<ImageEntity>)

    @Query("SELECT * FROM images_table WHERE imageId = :imageId")
    fun getImage(imageId: Long): Flow<ImageEntity>

    @Query("SELECT * FROM images_table WHERE searchQuery LIKE :query")
    fun getImages(query: String): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM images_table WHERE `searchQuery` = :query")
    suspend fun clearAll(query: String)
}