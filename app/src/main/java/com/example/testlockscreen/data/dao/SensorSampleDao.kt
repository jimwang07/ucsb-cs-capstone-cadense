package com.example.testlockscreen.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testlockscreen.data.entities.SensorSample

@Dao
interface SensorSampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSensorSample(sensorSample: SensorSample)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSensorSamples(sensorSamples: List<SensorSample>)

    @Query("SELECT * FROM sensor_samples WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getSensorSamplesForSession(sessionId: Long): List<SensorSample>

    @Query("DELETE FROM sensor_samples WHERE sessionId = :sessionId")
    suspend fun deleteSensorSamplesForSession(sessionId: Long)
}
