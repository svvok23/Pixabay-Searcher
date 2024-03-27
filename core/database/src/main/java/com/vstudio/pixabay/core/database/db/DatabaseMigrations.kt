package com.vstudio.pixabay.core.database.db

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object DatabaseMigrations {

    val Schema1to2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_images_table_searchQuery` ON `images_table` (`searchQuery`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_remote_keys_table_query` ON `remote_keys_table` (`query`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_remote_keys_table_createdAt` ON `remote_keys_table` (`createdAt`)")
        }
    }

    @RenameColumn.Entries(
        RenameColumn(
            tableName = "images_table",
            fromColumnName = "dbId",
            toColumnName = "id",
        )
    )
    class Schema2to3 : AutoMigrationSpec
}