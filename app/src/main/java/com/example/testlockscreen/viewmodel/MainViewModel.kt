package com.example.testlockscreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _metronomeBPM = MutableStateFlow(60)
    val metronomeBPM: StateFlow<Int> = _metronomeBPM.asStateFlow()

    private val _visualCueInterval = MutableStateFlow(1.0f)
    val visualCueInterval: StateFlow<Float> = _visualCueInterval.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(true)
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    private val _bluetoothStatus = MutableStateFlow("Not Connected") // TODO: Implement actual Bluetooth status
    val bluetoothStatus: StateFlow<String> = _bluetoothStatus.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun startSession(mode: String) {
        _isRecording.value = true
        _toastMessage.value = "Session started!"
    }

    fun stopSession() {
        _isRecording.value = false
        _toastMessage.value = "Session paused."
    }

    fun endSession(notes: String?, saveRemotely: Boolean = false) {
        _toastMessage.value = "Session ended."
        _isRecording.value = false
    }

    fun setMetronomeBPM(bpm: Int) {
        _metronomeBPM.value = bpm.coerceIn(40, 120)
    }

    fun increaseMetronomeBPM() {
        _metronomeBPM.value = (_metronomeBPM.value + 1).coerceAtMost(120)
    }

    fun decreaseMetronomeBPM() {
        _metronomeBPM.value = (_metronomeBPM.value - 1).coerceAtLeast(40)
    }

    fun setDefaultMetronomeBPM() {
        _metronomeBPM.value = 60
    }

    fun setVisualCueInterval(interval: Float) {
        _visualCueInterval.value = interval.coerceIn(0.5f, 3.0f)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _hapticsEnabled.value = enabled
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
