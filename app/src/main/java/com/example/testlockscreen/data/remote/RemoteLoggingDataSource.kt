package com.example.testlockscreen.data.remote

import com.example.testlockscreen.data.db.SessionWithSamples

interface RemoteLoggingDataSource {
    suspend fun uploadSession(sessionWithSamples: SessionWithSamples)
}

class StubRemoteLoggingDataSource : RemoteLoggingDataSource {
    override suspend fun uploadSession(sessionWithSamples: SessionWithSamples) {
        println("Queued for upload: $sessionWithSamples")
    }
}
