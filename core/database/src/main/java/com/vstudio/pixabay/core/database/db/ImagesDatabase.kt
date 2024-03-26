package com.vstudio.pixabay.core.database.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vstudio.pixabay.core.database.dao.ImageDao
import com.vstudio.pixabay.core.database.dao.RemoteKeyDao
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.RemoteKey
import com.vstudio.pixabay.core.database.util.Converters

@Database(
    entities = [ImageEntity::class, RemoteKey::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class ImagesDatabase : RoomDatabase() {
    abstract val imageDao: ImageDao
    abstract val remoteKeyDao: RemoteKeyDao
}