package com.example.testlockscreen.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SessionDao {
    @Transaction
    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSessionWithSamples(sessionId: Long): SessionWithSamples

    @Insert
    suspend fun insertSession(session: Session): Long

    @Insert
    suspend fun insertSamples(samples: List<SensorSample>)
}
