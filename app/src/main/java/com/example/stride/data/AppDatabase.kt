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

@Database(entities = [Session::class], version = 3)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a new table with the desired schema (without distance)
                database.execSQL("""
                    CREATE TABLE new_Session (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        duration INTEGER NOT NULL,
                        poleStrikes INTEGER NOT NULL,
                        onBeatPercent INTEGER NOT NULL,
                        avgOffsetMs INTEGER NOT NULL
                    )
                """.trimIndent())

                // Copy the data from the old table to the new table
                database.execSQL("""
                    INSERT INTO new_Session (id, timestamp, duration, poleStrikes, onBeatPercent, avgOffsetMs)
                    SELECT id, timestamp, duration, poleStrikes, onBeatPercent, avgOffsetMs FROM Session
                """.trimIndent())

                // Remove the old table
                database.execSQL("DROP TABLE Session")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE new_Session RENAME TO Session")
            }
        }


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "session_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}