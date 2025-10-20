package com.example.testlockscreen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val startTime: Long,
    val endTime: Long?,
    val mode: String,
    val notes: String?,
    val savedRemoteFlag: Boolean
)
