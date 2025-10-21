package com.example.testlockscreen.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testlockscreen.data.settingsDataStore
import com.example.testlockscreen.model.VisualColorOption
import com.example.testlockscreen.model.VisualColorOptions
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class SessionState {
        Idle,
        Running,
        Paused
    }

    private object PreferenceKeys {
        val BPM_KEY = intPreferencesKey("default_bpm")
        val COLOR_KEY = stringPreferencesKey("visual_color")
    }

    private val dataStore = application.settingsDataStore

    private val _sessionState = MutableStateFlow(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _connectionState = MutableStateFlow("Connected")
    val connectionState: StateFlow<String> = _connectionState

    private val _bpm = MutableStateFlow(60)
    val bpm: StateFlow<Int> = _bpm

    private val _visualColor = MutableStateFlow(VisualColorOptions.All.first())
    val visualColor: StateFlow<VisualColorOption> = _visualColor

    data class SessionSummary(
        val duration: String,
        val beats: Int
    )

    private val _sessionSummary = MutableStateFlow(SessionSummary("0:00", 0))
    val sessionSummary: StateFlow<SessionSummary> = _sessionSummary

    private var sessionStartMillis: Long? = null
    private var accumulatedMillis: Long = 0L

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                val savedBpm = preferences[PreferenceKeys.BPM_KEY] ?: 60
                val savedColor = preferences[PreferenceKeys.COLOR_KEY] ?: VisualColorOptions.All.first().preferenceValue
                _bpm.value = savedBpm
                _visualColor.value = VisualColorOptions.fromPreference(savedColor)
            }.collect {}
        }
    }

    fun startSession() {
        if (_sessionState.value != SessionState.Running) {
            if (_sessionState.value == SessionState.Idle) {
                accumulatedMillis = 0L
                sessionStartMillis = null
                _sessionSummary.value = SessionSummary("0:00", 0)
            }
            _sessionState.value = SessionState.Running
            _isRecording.value = true
            if (sessionStartMillis == null) {
                sessionStartMillis = System.currentTimeMillis()
            }
        }
    }

    fun stopSession() {
        if (_sessionState.value == SessionState.Running) {
            val now = System.currentTimeMillis()
            accumulatedMillis += now - (sessionStartMillis ?: now)
            sessionStartMillis = null
            _sessionState.value = SessionState.Paused
            _isRecording.value = false
        }
    }

    fun endSession() {
        val now = System.currentTimeMillis()
        val totalMillis = accumulatedMillis + if (_sessionState.value == SessionState.Running) {
            now - (sessionStartMillis ?: now)
        } else {
            0L
        }
        val durationText = formatDuration(totalMillis)
        val beats = ((totalMillis / 60_000.0) * _bpm.value).roundToInt()

        _sessionState.value = SessionState.Idle
        _isRecording.value = false
        sessionStartMillis = null
        accumulatedMillis = 0L

        _sessionSummary.value = SessionSummary(duration = durationText, beats = beats)
    }

    private fun formatDuration(totalMillis: Long): String {
        val totalSeconds = (totalMillis / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }
}
