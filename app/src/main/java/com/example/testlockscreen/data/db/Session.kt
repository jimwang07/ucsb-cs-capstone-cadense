package com.example.testlockscreen.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val mode: String,
    val bpmOrInterval: Float,
    val recorded: Boolean,
    val savedRemoteFlag: Boolean
)
