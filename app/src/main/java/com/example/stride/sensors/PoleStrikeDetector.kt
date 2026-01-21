package com.example.stride.sensors

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

data class PoleStrikeEvent(
    val timestamp: Long,
    val zAcceleration: Float
)

class PoleStrikeDetector(
    context: Context,
    private val scope: CoroutineScope
) {
    private val sensorManagerWrapper = SensorManagerWrapper(context)

    private val _strikeEvents = MutableSharedFlow<PoleStrikeEvent>()
    val strikeEvents: SharedFlow<PoleStrikeEvent> = _strikeEvents.asSharedFlow()

    private val strikeThreshold = 15f  // m/s^2
    private val strikeCooldownMs = 250L
    private var lastStrikeTimestamp = 0L

    fun startDetection() {
        scope.launch {
            sensorManagerWrapper.getAccelerometerSensorFlow().collect { sample ->
                detectPoleStrike(sample)
            }
        }
    }

    private suspend fun detectPoleStrike(sample: com.example.stride.data.entities.SensorSample) {
        val zValue = sample.value3 ?: return
        val timestamp = sample.timestamp

        if (abs(zValue) >= strikeThreshold &&
            timestamp - lastStrikeTimestamp >= strikeCooldownMs) {
            lastStrikeTimestamp = timestamp
            _strikeEvents.emit(PoleStrikeEvent(timestamp, zValue))
        }
    }
}
