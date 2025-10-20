package com.example.testlockscreen.data.repository

import com.example.testlockscreen.data.db.Session
import com.example.testlockscreen.data.db.SessionDao
import com.example.testlockscreen.data.db.SensorSample
import com.example.testlockscreen.data.remote.RemoteLoggingDataSource

class SessionRepository(
    private val sessionDao: SessionDao,
    private val remoteLoggingDataSource: RemoteLoggingDataSource
) {
    suspend fun saveSession(session: Session, samples: List<SensorSample>): Long {
        val sessionId = sessionDao.insertSession(session)
        val samplesWithSessionId = samples.map { it.copy(sessionId = sessionId) }
        sessionDao.insertSamples(samplesWithSessionId)
        // In a real app, you would use the remoteLoggingDataSource here
        return sessionId
    }
}
