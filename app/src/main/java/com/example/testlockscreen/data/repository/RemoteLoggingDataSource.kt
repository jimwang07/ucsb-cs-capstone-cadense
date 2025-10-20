package com.example.testlockscreen.data.repository

import com.example.testlockscreen.data.entities.Session
import com.example.testlockscreen.data.entities.SensorSample

interface RemoteLoggingDataSource {
    suspend fun uploadSession(session: Session, sensorSamples: List<SensorSample>): Result<Unit>
}
