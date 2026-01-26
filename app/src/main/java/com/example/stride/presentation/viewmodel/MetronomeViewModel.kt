package com.example.stride.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stride.data.AppDatabase
import com.example.stride.data.entities.Session
import com.example.stride.data.repository.LocationRepository
import com.example.stride.timing.TimingStats
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MetronomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionDao = AppDatabase.getDatabase(application).sessionDao()
    private val locationRepository = LocationRepository(sessionDao)

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _bpm = MutableStateFlow(60)
    val bpm = _bpm.asStateFlow()

    private val _beatCount = MutableStateFlow(0)
    val beatCount = _beatCount.asStateFlow()

    private val _stopwatch = MutableStateFlow(0L)
    val stopwatch = _stopwatch.asStateFlow()

    private val _distance = MutableStateFlow(0.0)
    val distance = _distance.asStateFlow()

    private val _lastBeatTimestamp = MutableStateFlow(0L)
    val lastBeatTimestamp = _lastBeatTimestamp.asStateFlow()

    private val _beatIntervalMs = MutableStateFlow(1000L)
    val beatIntervalMs = _beatIntervalMs.asStateFlow()

    private var metronomeJob: Job? = null
    private var stopwatchJob: Job? = null

    private var timeUntilNextBeat = 0L
    private var lastBeatTime = 0L

    private var stopwatchStartTime = 0L
    private var pausedStopwatchTime = 0L

    private val strideLengthConstant = 0.5 // meters

    /**
     * UPDATED: now saves timing stats too.
     */
    fun saveSession(duration: Int, distance: Int, poleStrikes: Int, timingStats: TimingStats) {
        viewModelScope.launch {
            val session = Session(
                timestamp = System.currentTimeMillis(),
                duration = duration,
                distance = distance,
                poleStrikes = poleStrikes,
                onBeatPercent = timingStats.onBeatPercentage.toInt(),
                avgOffsetMs = timingStats.averageOffsetMs.toInt()
            )
            locationRepository.insertSession(session)
        }
    }

    fun setBpm(newBpm: Int) {
        if (newBpm > 0) {
            _bpm.value = newBpm
            _beatIntervalMs.value = 60000L / newBpm
        }
    }

    fun toggle() {
        if (_isRunning.value) {
            pause()
        } else {
            start()
        }
    }

    private fun start() {
        // Prevent “multiple sessions”
        if (_isRunning.value) return

        _isRunning.value = true

        // Ensure jobs aren’t duplicated
        metronomeJob?.cancel()
        stopwatchJob?.cancel()

        stopwatchStartTime = System.currentTimeMillis()
        if (lastBeatTime == 0L) {
            lastBeatTime = System.currentTimeMillis()
        }

        metronomeJob = viewModelScope.launch {
            if (_beatCount.value == 0) {
                _beatCount.value++
                lastBeatTime = System.currentTimeMillis()
                _lastBeatTimestamp.value = lastBeatTime
                updateDistanceByStrikes()
            }

            while (isActive && _isRunning.value) {
                val delayMillis = 60000L / _bpm.value

                val delayToUse = if (timeUntilNextBeat > 0) {
                    val remaining = timeUntilNextBeat
                    timeUntilNextBeat = 0L
                    remaining
                } else {
                    delayMillis
                }

                delay(delayToUse)

                if (!isActive || !_isRunning.value) break

                _beatCount.value++
                lastBeatTime = System.currentTimeMillis()
                _lastBeatTimestamp.value = lastBeatTime
                updateDistanceByStrikes()
            }
        }

        stopwatchJob = viewModelScope.launch {
            while (isActive && _isRunning.value) {
                val elapsedTime =
                    pausedStopwatchTime + (System.currentTimeMillis() - stopwatchStartTime)
                _stopwatch.value = elapsedTime / 1000
                delay(100)
            }
        }
    }

    // Public so SessionScreen can pause on lifecycle/background.
    fun pause() {
        if (!_isRunning.value) return

        _isRunning.value = false
        metronomeJob?.cancel()
        stopwatchJob?.cancel()

        pausedStopwatchTime += System.currentTimeMillis() - stopwatchStartTime

        val delayMillis = 60000L / _bpm.value
        val elapsedSinceLastBeat = System.currentTimeMillis() - lastBeatTime
        timeUntilNextBeat = if (elapsedSinceLastBeat < delayMillis) {
            delayMillis - elapsedSinceLastBeat
        } else {
            0L
        }
    }

    fun stop() {
        _isRunning.value = false

        metronomeJob?.cancel()
        stopwatchJob?.cancel()
        metronomeJob = null
        stopwatchJob = null

        _beatCount.value = 0
        _stopwatch.value = 0
        _distance.value = 0.0

        timeUntilNextBeat = 0L
        lastBeatTime = 0L
        _lastBeatTimestamp.value = 0L
        pausedStopwatchTime = 0L
    }

    private fun updateDistanceByStrikes() {
        _distance.value = _beatCount.value * strideLengthConstant
    }
}