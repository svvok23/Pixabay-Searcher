package com.vstudio.pixabay.core.database.di

import android.content.Context
import androidx.room.Room
import com.vstudio.pixabay.core.database.ImagesLocalDataSource
import com.vstudio.pixabay.core.database.QueriesLocalDataSource
import com.vstudio.pixabay.core.database.datasource.ImagesRoomDataSource
import com.vstudio.pixabay.core.database.datasource.QueriesRoomDataSource
import com.vstudio.pixabay.core.database.db.DatabaseMigrations
import com.vstudio.pixabay.core.database.db.ImagesDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatabaseModule {

    @Binds
    abstract fun bindsImagesLocalDataSource(
        imagesRoomDataSource: ImagesRoomDataSource,
    ): ImagesLocalDataSource

    @Binds
    abstract fun bindsQueriesLocalDataSource(
        queriesLocalDataSource: QueriesRoomDataSource,
    ): QueriesLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideImagesDatabase(@ApplicationContext context: Context): ImagesDatabase {
            return Room.databaseBuilder(
                context,
                ImagesDatabase::class.java,
                "images.db"
            )
                .addMigrations(DatabaseMigrations.Schema1to2)
                .build()
        }
    }
}