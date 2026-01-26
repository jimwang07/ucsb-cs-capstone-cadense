package com.example.stride.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stride.sensors.PoleStrikeDetector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TapBpmViewModel(application: Application) : AndroidViewModel(application) {

    private val poleStrikeDetector = PoleStrikeDetector(application, viewModelScope)

    private val _tapTimestamps = MutableStateFlow<List<Long>>(emptyList())
    val tapTimestamps = _tapTimestamps.asStateFlow()

    private val _isAutoDetectEnabled = MutableStateFlow(false)
    val isAutoDetectEnabled = _isAutoDetectEnabled.asStateFlow()

    private val _strikeDetectedEvent = MutableSharedFlow<Unit>()
    val strikeDetectedEvent: SharedFlow<Unit> = _strikeDetectedEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            poleStrikeDetector.strikeEvents.collect { event ->
                if (_isAutoDetectEnabled.value) {
                    processTap(event.timestamp)
                    _strikeDetectedEvent.emit(Unit)
                }
            }
        }
    }

    fun toggleAutoDetect() {
        _isAutoDetectEnabled.value = !_isAutoDetectEnabled.value
        if (_isAutoDetectEnabled.value) {
            poleStrikeDetector.startDetection()
        }
    }

    fun onManualTap() {
        processTap(System.currentTimeMillis())
    }

    private fun processTap(timestamp: Long) {
        val currentList = _tapTimestamps.value
        _tapTimestamps.value = (currentList + timestamp).takeLast(8)
    }

    fun reset() {
        _tapTimestamps.value = emptyList()
    }

    fun getCalculatedBpm(): Int {
        val timestamps = _tapTimestamps.value
        if (timestamps.size < 2) return 0
        
        val intervals = timestamps.zipWithNext { a, b -> b - a }
        val averageInterval = intervals.average()
        return if (averageInterval > 0) (60000 / averageInterval).toInt() else 0
    }
}
