package com.example.testlockscreen.data.repository

import com.example.testlockscreen.data.dao.SessionDao
import com.example.testlockscreen.data.dao.SensorSampleDao
import com.example.testlockscreen.data.entities.Session
import com.example.testlockscreen.data.entities.SensorSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class LocalLoggingRepository(
    private val sessionDao: SessionDao,
    private val sensorSampleDao: SensorSampleDao
) : LoggingRepository {

    override suspend fun startSession(mode: String): Long = withContext(Dispatchers.IO) {
        val session = Session(startTime = System.currentTimeMillis(), endTime = null, mode = mode, notes = null, savedRemoteFlag = false)
        sessionDao.insertSession(session)
    }

    override suspend fun endSession(sessionId: Long, notes: String?, saveRemotely: Boolean) {
        withContext(Dispatchers.IO) {
            val session = sessionDao.getSessionById(sessionId)?.copy(
                endTime = System.currentTimeMillis(),
                notes = notes,
                savedRemoteFlag = saveRemotely
            )
            session?.let { sessionDao.updateSession(it) }
        }
    }

    override suspend fun addSensorSample(sessionId: Long, sensorSample: SensorSample) {
        withContext(Dispatchers.IO) {
            sensorSampleDao.insertSensorSample(sensorSample.copy(sessionId = sessionId))
        }
    }

    override suspend fun getSessionWithSamples(sessionId: Long): Pair<Session, List<SensorSample>>? = withContext(Dispatchers.IO) {
        val session = sessionDao.getSessionById(sessionId)
        val samples = sensorSampleDao.getSensorSamplesForSession(sessionId)
        if (session != null) Pair(session, samples) else null
    }

    override fun getAllSessions(): Flow<List<Session>> = flow {
        emit(sessionDao.getAllSessions())
    }

    override suspend fun deleteSession(sessionId: Long) {
        withContext(Dispatchers.IO) {
            sessionDao.deleteSessionById(sessionId)
            sensorSampleDao.deleteSensorSamplesForSession(sessionId)
        }
    }
}
