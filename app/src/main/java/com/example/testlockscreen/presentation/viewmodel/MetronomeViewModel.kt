package com.example.testlockscreen.presentation.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testlockscreen.data.location.LocationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MetronomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository(application)
    private var locationJob: Job? = null

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _bpm = MutableStateFlow(60)
    val bpm = _bpm.asStateFlow()

    private val _beatCount = MutableStateFlow(0)
    val beatCount = _beatCount.asStateFlow()

    private val _stopwatch = MutableStateFlow(0L)
    val stopwatch = _stopwatch.asStateFlow()

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private val _distance = MutableStateFlow(0.0)
    val distance = _distance.asStateFlow()

    private var metronomeJob: Job? = null
    private var stopwatchJob: Job? = null

    private var timeUntilNextBeat = 0L
    private var lastBeatTime = 0L // Time of the last beat

    private var stopwatchStartTime = 0L
    private var pausedStopwatchTime = 0L

    private var lastLocation: Location? = null

    fun onLocationPermissionGranted() {
        Log.d("MetronomeViewModel", "onLocationPermissionGranted called")
    }

    private fun startLocationUpdates() {
        Log.d("MetronomeViewModel", "Starting location updates...")
        locationJob = locationRepository.getLocationUpdates()
            .catch { e -> Log.e("MetronomeViewModel", "Error getting location", e) }
            .onEach { location ->
                Log.d("MetronomeViewModel", "New location: $location")
                _location.value = location
                lastLocation?.let {
                    _distance.value += location.distanceTo(it)
                }
                lastLocation = location
            }
            .launchIn(viewModelScope)
    }

    private fun stopLocationUpdates() {
        Log.d("MetronomeViewModel", "Stopping location updates")
        locationJob?.cancel()
    }

    fun setBpm(newBpm: Int) {
        if (newBpm > 0) {
            _bpm.value = newBpm
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
        _isRunning.value = true
        startLocationUpdates()
        stopwatchStartTime = System.currentTimeMillis()
        if (lastBeatTime == 0L) { // If it's the very first start
            lastBeatTime = System.currentTimeMillis()
        }

        metronomeJob = viewModelScope.launch {
            if (_beatCount.value == 0) {
                _beatCount.value++
            }
            while (isActive) {
                val delayMillis = 60000L / _bpm.value

                val delayToUse = if (timeUntilNextBeat > 0) {
                    val remaining = timeUntilNextBeat
                    timeUntilNextBeat = 0L // Consume it
                    remaining
                } else {
                    delayMillis
                }

                delay(delayToUse)

                if (!isRunning.value) break // Check again after delay

                _beatCount.value++
                lastBeatTime = System.currentTimeMillis()
            }
        }

        stopwatchJob = viewModelScope.launch {
            while (_isRunning.value) {
                val elapsedTime = pausedStopwatchTime + (System.currentTimeMillis() - stopwatchStartTime)
                _stopwatch.value = elapsedTime / 1000
                delay(100) // Update UI frequently
            }
        }
    }

    private fun pause() {
        _isRunning.value = false
        stopLocationUpdates()
        metronomeJob?.cancel()
        stopwatchJob?.cancel()

        // Update stopwatch time
        pausedStopwatchTime += System.currentTimeMillis() - stopwatchStartTime

        // Calculate time remaining for the next beat
        val delayMillis = 60000L / _bpm.value
        val elapsedSinceLastBeat = System.currentTimeMillis() - lastBeatTime
        timeUntilNextBeat = if (elapsedSinceLastBeat < delayMillis) {
            delayMillis - elapsedSinceLastBeat
        } else {
            0L // If we paused after a beat was due, just start a new beat on resume
        }
    }

    fun stop() {
        _isRunning.value = false
        stopLocationUpdates()
        _beatCount.value = 0
        _stopwatch.value = 0
        _distance.value = 0.0
        lastLocation = null
        timeUntilNextBeat = 0L
        lastBeatTime = 0L
        pausedStopwatchTime = 0L
        metronomeJob?.cancel()
        stopwatchJob?.cancel()
    }

    fun formatStopwatch(time: Long): String {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
