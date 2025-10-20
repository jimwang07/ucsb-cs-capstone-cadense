package com.example.testlockscreen.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_samples")
data class SensorSample(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val timestampMs: Long,
    val type: String,
    val v1: Float,
    val v2: Float,
    val v3: Float
)
