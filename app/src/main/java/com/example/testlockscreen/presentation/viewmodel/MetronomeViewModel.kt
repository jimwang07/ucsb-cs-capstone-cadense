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
        metronomeJob = viewModelScope.launch {
            while (_isRunning.value) {
                _beatCount.value++
                val delayMillis = 60000L / _bpm.value
                delay(delayMillis)
            }
        }
        stopwatchJob = viewModelScope.launch {
            while (_isRunning.value) {
                delay(1000)
                _stopwatch.value++
            }
        }
    }

    private fun pause() {
        _isRunning.value = false
        metronomeJob?.cancel()
        stopwatchJob?.cancel()
    }

    fun stop() {
        _isRunning.value = false
        _beatCount.value = 0
        _stopwatch.value = 0
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
