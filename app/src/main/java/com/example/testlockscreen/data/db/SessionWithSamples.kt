package com.example.testlockscreen.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithSamples(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val samples: List<SensorSample>
)
