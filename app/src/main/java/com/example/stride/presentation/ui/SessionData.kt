package com.example.stride.presentation.ui

import com.example.stride.timing.TimingStats

/**
 * A plain data class to hold session data for UI display.
 */
data class SessionData(
    val time: Int,
    val poleStrikes: Int,
    val timingStats: TimingStats = TimingStats()
)
