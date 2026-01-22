// =======================
// AppDatabase.kt (UPDATED)
// =======================
package com.example.stride.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stride.data.dao.SessionDao
import com.example.stride.data.entities.Session

@Database(entities = [Session::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration: v1 -> v2 adds timing stats columns
        // Note: Room requires NOT NULL columns added via ALTER TABLE to have a DEFAULT.
        // Using 0 keeps your "no -1" requirement while allowing migration of old rows.
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE Session ADD COLUMN onBeatPercent INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE Session ADD COLUMN avgOffsetMs INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "session_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}