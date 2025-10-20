package com.example.testlockscreen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_samples")
data class SensorSample(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val timestamp: Long,
    val type: String,
    val value1: Float?,
    val value2: Float?,
    val value3: Float?,
    var sessionId: Long = 0L // Default value, will be updated by the repository
)
