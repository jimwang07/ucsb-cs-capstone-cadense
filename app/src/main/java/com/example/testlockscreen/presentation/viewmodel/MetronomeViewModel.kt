package com.example.testlockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MetronomeViewModel : ViewModel() {

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _bpm = MutableStateFlow(60)
    val bpm = _bpm.asStateFlow()

    private val _beatCount = MutableStateFlow(0)
    val beatCount = _beatCount.asStateFlow()

    private val _stopwatch = MutableStateFlow(0L)
    val stopwatch = _stopwatch.asStateFlow()

    private var metronomeJob: Job? = null
    private var stopwatchJob: Job? = null

    private var timeUntilNextBeat = 0L
    private var lastTickTime = 0L

    private var stopwatchStartTime = 0L
    private var pausedStopwatchTime = 0L

    fun setBpm(newBpm: Int) {
        _bpm.value = newBpm
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
        lastTickTime = System.currentTimeMillis()
        stopwatchStartTime = System.currentTimeMillis()
        metronomeJob = viewModelScope.launch {
            val delayMillis = 60000L / _bpm.value

            if (_beatCount.value == 0) {
                // First ever start
                _beatCount.value++
                lastTickTime = System.currentTimeMillis()
                delay(delayMillis)
            } else if (timeUntilNextBeat > 0) {
                // Resuming from pause
                delay(timeUntilNextBeat)
            } else {
                // Fallback
                delay(delayMillis)
            }

            // Main loop
            while (_isRunning.value) {
                _beatCount.value++
                timeUntilNextBeat = 0L
                lastTickTime = System.currentTimeMillis()
                delay(delayMillis)
            }
        }

        stopwatchJob = viewModelScope.launch {
            while (_isRunning.value) {
                val elapsedTime = pausedStopwatchTime + (System.currentTimeMillis() - stopwatchStartTime)
                _stopwatch.value = elapsedTime / 1000
                delay(100)
            }
        }
    }

    private fun pause() {
        _isRunning.value = false
        metronomeJob?.cancel()
        stopwatchJob?.cancel()

        pausedStopwatchTime += System.currentTimeMillis() - stopwatchStartTime

        val delayMillis = 60000L / _bpm.value
        val elapsedTime = System.currentTimeMillis() - lastTickTime

        timeUntilNextBeat = if (elapsedTime < delayMillis) {
            delayMillis - elapsedTime
        } else {
            0L
        }
    }

    fun stop() {
        _isRunning.value = false
        _beatCount.value = 0
        _stopwatch.value = 0
        timeUntilNextBeat = 0L
        lastTickTime = 0L
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
