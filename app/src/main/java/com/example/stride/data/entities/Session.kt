package com.example.stride.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val duration: Int,
    val distance: Int,
    val poleStrikes: Int,
    val onBeatPercent: Int,
    val avgOffsetMs: Int
)