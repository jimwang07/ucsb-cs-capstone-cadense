package com.example.testlockscreen.data.repository

import com.example.testlockscreen.data.entities.Session
import com.example.testlockscreen.data.entities.SensorSample
import kotlinx.coroutines.flow.Flow

interface LoggingRepository {
    suspend fun startSession(mode: String): Long
    suspend fun endSession(sessionId: Long, notes: String? = null, saveRemotely: Boolean = false)
    suspend fun addSensorSample(sessionId: Long, sensorSample: SensorSample)
    suspend fun getSessionWithSamples(sessionId: Long): Pair<Session, List<SensorSample>>?
    fun getAllSessions(): Flow<List<Session>>
    suspend fun deleteSession(sessionId: Long)
}
