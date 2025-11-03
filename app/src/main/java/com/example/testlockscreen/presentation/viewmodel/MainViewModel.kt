package com.example.testlockscreen.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.testlockscreen.data.db.AppDatabase
import com.example.testlockscreen.data.remote.StubRemoteLoggingDataSource
import com.example.testlockscreen.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class SessionState {
        Idle,
        Running,
        Paused
    }

    data class SessionSummary(
        val elapsedTime: String,
        val mode: String,
        val count: Int
    )

    private val sessionRepository: SessionRepository

    init {
        val sessionDao = AppDatabase.getDatabase(application).sessionDao()
        val remoteDataSource = StubRemoteLoggingDataSource()
        sessionRepository = SessionRepository(sessionDao, remoteDataSource)
    }

    private val _sessionState = MutableStateFlow(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _bpm = MutableStateFlow(60)
    val bpm: StateFlow<Int> = _bpm

    private val _interval = MutableStateFlow(1.0)
    val interval: StateFlow<Double> = _interval

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _connectionState = MutableStateFlow("Connected")
    val connectionState: StateFlow<String> = _connectionState

    private val _mode = MutableStateFlow("Metronome")
    val mode: StateFlow<String> = _mode
    
    private val _sessionSummary = MutableStateFlow(SessionSummary("0s", "N/A", 0))
    val sessionSummary: StateFlow<SessionSummary> = _sessionSummary

    fun startSession() {
        _sessionState.value = SessionState.Running
        _isRecording.value = true
    }

    fun stopSession() {
        _sessionState.value = SessionState.Paused
        _isRecording.value = false
    }

    fun endSession() {
        _sessionState.value = SessionState.Idle
        _isRecording.value = false
        // This would be calculated based on real data
        _sessionSummary.value = SessionSummary("1m 23s", _mode.value, 150)
    }
    
    fun saveSession() {
        // Here you would implement saving logic with the repository
    }

    fun discardSession() {
        // Nothing to do here if we are not persisting anything yet
    }

    fun incBpm() {
        _bpm.value = (_bpm.value + 1).coerceIn(40, 120)
    }

    fun decBpm() {
        _bpm.value = (_bpm.value - 1).coerceIn(40, 120)
    }

    fun setBpm(newBpm: Int) {
        _bpm.value = newBpm.coerceIn(40, 120)
    }

    fun incInterval() {
        _interval.value = (_interval.value + 0.5).coerceIn(0.5, 3.0)
    }

    fun decInterval() {
        _interval.value = (_interval.value - 0.5).coerceIn(0.5, 3.0)
    }

    fun setInterval(newInterval: Double) {
        _interval.value = newInterval.coerceIn(0.5, 3.0)
    }
}
