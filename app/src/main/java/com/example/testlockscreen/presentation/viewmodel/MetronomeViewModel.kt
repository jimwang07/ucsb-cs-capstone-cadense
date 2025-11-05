package com.example.testlockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
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
    private var lastBeatTime = 0L // Time of the last beat

    private var stopwatchStartTime = 0L
    private var pausedStopwatchTime = 0L

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
        _beatCount.value = 0
        _stopwatch.value = 0
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
