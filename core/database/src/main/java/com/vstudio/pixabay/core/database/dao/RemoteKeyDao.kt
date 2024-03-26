package com.vstudio.pixabay.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vstudio.pixabay.core.database.model.RemoteKey
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys_table WHERE id = :id")
    suspend fun getKey(id: Long): RemoteKey?

    @Query("SELECT `query` FROM remote_keys_table GROUP BY `query` ORDER BY createdAt DESC")
    fun getQueriesFlow(): Flow<List<String>>

    @Query("SELECT createdAt FROM remote_keys_table WHERE `query` = :query ORDER BY createdAt ASC LIMIT 1")
    suspend fun getOldestCreationTime(query: String): Long?

    @Query("DELETE FROM remote_keys_table WHERE `query` = :query")
    suspend fun clearAll(query: String)
}