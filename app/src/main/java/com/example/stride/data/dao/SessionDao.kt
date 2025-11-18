package com.example.stride.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stride.data.entities.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: Session)

    @Query("SELECT * FROM session ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<Session>>
}