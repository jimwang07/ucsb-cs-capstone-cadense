package com.example.stride.timing

data class TimingStats(
    val totalStrikes: Int = 0,
    val onBeatStrikes: Int = 0,
    val offBeatStrikes: Int = 0,
    val totalAbsoluteOffsetMs: Long = 0L
) {
    val onBeatPercentage: Float
        get() = if (totalStrikes > 0) (onBeatStrikes.toFloat() / totalStrikes) * 100f else 0f

    val averageOffsetMs: Float
        get() = if (totalStrikes > 0) totalAbsoluteOffsetMs.toFloat() / totalStrikes else 0f
}
