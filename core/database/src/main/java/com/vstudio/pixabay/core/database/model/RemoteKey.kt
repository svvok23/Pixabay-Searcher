package com.vstudio.pixabay.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "remote_keys_table",
    indices = [
        androidx.room.Index(value = ["query"], unique = false),
        androidx.room.Index(value = ["createdAt"], unique = false),
    ]
)
data class RemoteKey(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val query: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val createdAt: Long,
)