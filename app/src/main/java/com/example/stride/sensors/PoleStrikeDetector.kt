package com.example.stride.sensors

import android.content.Context
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
    private val peakWindowMs: Long = 300,
    private val maxPeakWidthMs: Long = 40,
    private val minRiseRateMs: Float = 0.5f,  // m/s² per ms — minimum slope to count as a strike
    private val riseRateWindowMs: Long = 10   // measure slope over this window
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

    // Peak tracking
    private var peakStartTimestamp: Long = 0
    private var peakSampleCount: Int = 0
    private var peakMaxValue: Float = 0f
    private var trackingPeak = false
    private var peakRiseRate: Float = 0f

    // Slope tracking
    private var lastSampleTimestamp: Long = 0
    private var lastSampleValue: Float = 0f

    companion object {
        private const val SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST
    }

    fun startDetection() {
        dataWindow.clear()
        filteredDataWindow.clear()
        avgFilter = 0.0f
        stdFilter = 0.0f
        lastStrikeTimestamp = 0

        peakStartTimestamp = 0
        peakSampleCount = 0
        peakMaxValue = 0f
        trackingPeak = false
        peakRiseRate = 0f

        lastSampleTimestamp = 0
        lastSampleValue = 0f

        detectionJob = scope.launch {
            sensorManagerWrapper.getAccelerometerSensorFlow(SAMPLING_PERIOD_US).collect { sample ->
                detectPoleStrike(sample)
            }
        }
    }

    fun stopDetection() {
        detectionJob?.cancel()
        detectionJob = null
    }

    private suspend fun detectPoleStrike(sample: com.example.stride.data.entities.SensorSample) {

        val zValue = sample.value3 ?: return
        val timestamp = sample.timestamp

        // Calculate instantaneous rise rate (slope) in m/s² per ms
        val dt = if (lastSampleTimestamp > 0) (timestamp - lastSampleTimestamp).toFloat() else 1f
        val riseRate = (zValue - lastSampleValue) / dt
        lastSampleTimestamp = timestamp
        lastSampleValue = zValue

        val isInCooldown = (timestamp - lastStrikeTimestamp) <= peakWindowMs
        if (isInCooldown) {
            updateWindows(zValue, zValue)
            recalculateFilters()
            return
        }

        val isSignificantPeak =
            zValue > avgFilter && (zValue - avgFilter) > threshold * stdFilter

        val isAboveAbsoluteThreshold = zValue > absoluteThreshold

        val isPeakCandidate = isSignificantPeak && isAboveAbsoluteThreshold

        if (isPeakCandidate) {

            if (!trackingPeak) {
                trackingPeak = true
                peakStartTimestamp = timestamp
                peakSampleCount = 1
                peakMaxValue = zValue
                peakRiseRate = riseRate  // Capture leading-edge slope
            } else {
                peakSampleCount++
                if (zValue > peakMaxValue) peakMaxValue = zValue
                if (riseRate > peakRiseRate) peakRiseRate = riseRate
            }

        } else if (trackingPeak) {

            val peakWidth = timestamp - peakStartTimestamp
            val isNarrowEnough = peakWidth <= maxPeakWidthMs
            val isSteepEnough = peakRiseRate >= minRiseRateMs  // Reject slow-rising jerks

            if (isNarrowEnough && isSteepEnough) {
                _strikeEvents.emit(
                    PoleStrikeEvent(
                        peakStartTimestamp,
                        peakMaxValue
                    )
                )
                lastStrikeTimestamp = timestamp
            }

            trackingPeak = false
            peakSampleCount = 0
            peakMaxValue = 0f
            peakRiseRate = 0f
        }

        val lastFilteredValue =
            if (filteredDataWindow.isNotEmpty()) filteredDataWindow.last() else zValue

        val filteredValue =
            influence * zValue + (1 - influence) * lastFilteredValue

        updateWindows(zValue, filteredValue)
        recalculateFilters()
    }

    private fun recalculateFilters() {
        avgFilter = calculateMean(filteredDataWindow)
        stdFilter = calculateStdDev(filteredDataWindow, avgFilter)
    }

    private fun updateWindows(newValue: Float, newFilteredValue: Float) {
        dataWindow.addLast(newValue)
        filteredDataWindow.addLast(newFilteredValue)

        if (dataWindow.size > lag) {
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
