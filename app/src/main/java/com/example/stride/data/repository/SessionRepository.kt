package com.example.stride.data.repository

import com.example.stride.data.dao.SessionDao
import com.example.stride.data.entities.Session
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insertSession(session: Session) {
        sessionDao.insert(session)
    }

    fun getAllSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions()
    }
}
