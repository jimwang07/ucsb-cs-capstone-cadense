package com.example.stride.sensors

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class PoleStrikeEvent(
    val timestamp: Long,
    val zAcceleration: Float
)

class PoleStrikeDetector(
    private val context: Context,
    private val scope: CoroutineScope,
    private val lag: Int = 25,
    private val threshold: Float = 5.0f,
    private val influence: Float = 0.1f,
    private val absoluteThreshold: Float = 15.0f,
    private val peakWindowMs: Long = 300 // Cooldown window after a strike
) {
    private val sensorManagerWrapper = SensorManagerWrapper(context)

    private val _strikeEvents = MutableSharedFlow<PoleStrikeEvent>()
    val strikeEvents: SharedFlow<PoleStrikeEvent> = _strikeEvents.asSharedFlow()

    private var detectionJob: Job? = null

    // Algorithm state variables
    private val dataWindow = ArrayDeque<Float>(lag)
    private val filteredDataWindow = ArrayDeque<Float>(lag)
    private var avgFilter = 0.0f
    private var stdFilter = 0.0f

    // Cooldown tracking
    private var lastStrikeTimestamp: Long = 0

    fun startDetection() {
        dataWindow.clear()
        filteredDataWindow.clear()
        avgFilter = 0.0f
        stdFilter = 0.0f
        lastStrikeTimestamp = 0

        detectionJob = scope.launch {
            sensorManagerWrapper.getAccelerometerSensorFlow().collect { sample ->
                detectPoleStrike(sample)
            }
        }
    }

       private suspend fun detectPoleStrike(sample: com.example.stride.data.entities.SensorSample) {
        val zValue = sample.value3 ?: return
        val timestamp = sample.timestamp

        // Check if we are in the cooldown period
        val isInCooldown = (timestamp - lastStrikeTimestamp) <= peakWindowMs
        if (isInCooldown) {
            // Still update windows to keep the filter current
            updateWindows(zValue, zValue)
            recalculateFilters()
            return
        }

        val isSignificantPeak = zValue > avgFilter && (zValue - avgFilter) > threshold * stdFilter
        val isAboveAbsoluteThreshold = zValue > absoluteThreshold

        if (isSignificantPeak && isAboveAbsoluteThreshold) {
            // Emit the event immediately
            _strikeEvents.emit(PoleStrikeEvent(timestamp, zValue))
            // Start the cooldown
            lastStrikeTimestamp = timestamp

            // Update filtered value with influence
            val lastFilteredValue = if (filteredDataWindow.isNotEmpty()) filteredDataWindow.last() else zValue
            val filteredValue = influence * zValue + (1 - influence) * lastFilteredValue
            updateWindows(zValue, filteredValue)
        } else {
            // No signal, update with the actual value
            updateWindows(zValue, zValue)
        }

        recalculateFilters()
    }

    private fun recalculateFilters() {
        // Recalculate the moving average and standard deviation
        avgFilter = calculateMean(filteredDataWindow)
        stdFilter = calculateStdDev(filteredDataWindow, avgFilter)
    }

    private fun updateWindows(newValue: Float, newFilteredValue: Float) {
        dataWindow.addLast(newValue)
        filteredDataWindow.addLast(newFilteredValue)

        if(dataWindow.size > lag) {
            filteredDataWindow.removeFirst()
            dataWindow.removeFirst()
        }
    }

    private fun calculateMean(window: Collection<Float>): Float {
        if (window.isEmpty()) return 0f
        return window.sum() / window.size
    }

    private fun calculateStdDev(window: Collection<Float>, mean: Float): Float {
        if (window.size < 2) return 0f
        val sumOfSquares = window.sumOf { (it - mean).pow(2).toDouble() }
        return sqrt(sumOfSquares / (window.size - 1)).toFloat()
    }
}