package com.example.stride.presentation.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stride.data.AppDatabase
import com.example.stride.data.entities.Session
import com.example.stride.data.repository.LocationRepository
import com.example.stride.services.LocationService
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

    private val sessionDao = AppDatabase.getDatabase(application).sessionDao()
    private val locationRepository = LocationRepository(sessionDao)
    private val locationService = LocationService(application)
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
    private var lastBeatTime = 0L 

    private var stopwatchStartTime = 0L
    private var pausedStopwatchTime = 0L

    private var lastLocation: Location? = null

    fun onLocationPermissionGranted() {
        Log.d("MetronomeViewModel", "onLocationPermissionGranted called")
    }

    fun saveSession(duration: Int, distance: Int, poleStrikes: Int) {
        viewModelScope.launch {
            val session = Session(
                timestamp = System.currentTimeMillis(),
                duration = duration,
                distance = distance,
                poleStrikes = poleStrikes
            )
            locationRepository.insertSession(session)
        }
    }

    private fun startLocationUpdates() {
        locationJob?.cancel()
        locationJob = locationService.getLocationUpdates()
            .catch { e -> Log.e("MetronomeViewModel", "Error getting location", e) }
            .onEach { location ->
                _location.value = location
                lastLocation?.let {
                    _distance.value += location.distanceTo(it)
                }
                lastLocation = location
            }
            .launchIn(viewModelScope)
    }

    private fun stopLocationUpdates() {
        locationJob?.cancel()
        locationJob = null
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
        if (_isRunning.value) return
        _isRunning.value = true
        startLocationUpdates()
        stopwatchStartTime = System.currentTimeMillis()
        if (lastBeatTime == 0L) {
            lastBeatTime = System.currentTimeMillis()
        }

        metronomeJob?.cancel()
        metronomeJob = viewModelScope.launch {
            if (_beatCount.value == 0) {
                _beatCount.value++
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
            }
        }

        stopwatchJob?.cancel()
        stopwatchJob = viewModelScope.launch {
            while (isActive && _isRunning.value) {
                val elapsedTime = pausedStopwatchTime + (System.currentTimeMillis() - stopwatchStartTime)
                _stopwatch.value = elapsedTime / 1000
                delay(100)
            }
        }
    }

    fun pause() {
        if (!_isRunning.value) return
        _isRunning.value = false
        stopLocationUpdates()
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
        stopLocationUpdates()
        metronomeJob?.cancel()
        stopwatchJob?.cancel()
        metronomeJob = null
        stopwatchJob = null
        
        _beatCount.value = 0
        _stopwatch.value = 0
        _distance.value = 0.0
        lastLocation = null
        timeUntilNextBeat = 0L
        lastBeatTime = 0L
        pausedStopwatchTime = 0L
    }

    fun formatStopwatch(time: Long): String {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
