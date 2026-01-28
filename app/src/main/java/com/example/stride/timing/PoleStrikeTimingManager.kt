package com.example.stride.timing

import com.example.stride.sensors.PoleStrikeEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class PoleStrikeTimingManager(
    private val scope: CoroutineScope,
    private val onBeatThresholdMs: Long = 200L,
    private val feedbackDurationMs: Long = 300L
) {
    private val _timingFeedback = MutableStateFlow<TimingFeedback>(TimingFeedback.None)
    val timingFeedback: StateFlow<TimingFeedback> = _timingFeedback.asStateFlow()

    private val _stats = MutableStateFlow(TimingStats())
    val stats: StateFlow<TimingStats> = _stats.asStateFlow()

    private val _strikeCount = MutableStateFlow(0)
    val strikeCount: StateFlow<Int> = _strikeCount.asStateFlow()

    private var feedbackJob: Job? = null

    fun processStrike(
        strikeEvent: PoleStrikeEvent,
        lastBeatTimestamp: Long,
        beatIntervalMs: Long
    ) {
        // Don't process if metronome hasn't started yet
        if (lastBeatTimestamp == 0L || beatIntervalMs == 0L) return

        val offset = calculateTimingOffset(
            strikeEvent.timestamp,
            lastBeatTimestamp,
            beatIntervalMs
        )

        val isOnBeat = abs(offset) <= onBeatThresholdMs

        // Update feedback
        _timingFeedback.value = if (isOnBeat) {
            TimingFeedback.OnBeat(offset)
        } else {
            TimingFeedback.OffBeat(offset)
        }

        // Clear feedback after duration
        feedbackJob?.cancel()
        feedbackJob = scope.launch {
            delay(feedbackDurationMs)
            _timingFeedback.value = TimingFeedback.None
        }

        // Update stats
        _strikeCount.value++
        _stats.value = _stats.value.copy(
            totalStrikes = _stats.value.totalStrikes + 1,
            onBeatStrikes = _stats.value.onBeatStrikes + if (isOnBeat) 1 else 0,
            offBeatStrikes = _stats.value.offBeatStrikes + if (!isOnBeat) 1 else 0,
            totalAbsoluteOffsetMs = _stats.value.totalAbsoluteOffsetMs + abs(offset)
        )
    }

    private fun calculateTimingOffset(
        strikeTimestamp: Long,
        lastBeatTimestamp: Long,
        beatIntervalMs: Long
    ): Long {
        val timeSinceLastBeat = strikeTimestamp - lastBeatTimestamp
        val normalizedTime = timeSinceLastBeat % beatIntervalMs
        val timeUntilNextBeat = beatIntervalMs - normalizedTime

        return if (normalizedTime <= timeUntilNextBeat) {
            normalizedTime  // Late from last beat (positive)
        } else {
            -timeUntilNextBeat  // Early for next beat (negative)
        }
    }

    fun reset() {
        _stats.value = TimingStats()
        _strikeCount.value = 0
        _timingFeedback.value = TimingFeedback.None
        feedbackJob?.cancel()
    }
}
