package com.example.stride.presentation.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Application.dataStore by preferencesDataStore("settings")

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    private object PreferenceKeys {
        val BPM_KEY = intPreferencesKey("default_bpm")
        val VISUAL_ENABLED_KEY = booleanPreferencesKey("visual_enabled")
        val AUDIO_ENABLED_KEY = booleanPreferencesKey("audio_enabled")
        val VIBRATION_ENABLED_KEY = booleanPreferencesKey("vibration_enabled")
    }

    private val _defaultBpm = MutableStateFlow(60)
    val defaultBpm: StateFlow<Int> = _defaultBpm

    private val _isVisualEnabled = MutableStateFlow(true)
    val isVisualEnabled: StateFlow<Boolean> = _isVisualEnabled

    private val _isAudioEnabled = MutableStateFlow(true)
    val isAudioEnabled: StateFlow<Boolean> = _isAudioEnabled

    private val _isVibrationEnabled = MutableStateFlow(false) // Default to off
    val isVibrationEnabled: StateFlow<Boolean> = _isVibrationEnabled

    init {
        viewModelScope.launch {
            dataStore.data.map {
                _defaultBpm.value = it[PreferenceKeys.BPM_KEY] ?: 60
                _isVisualEnabled.value = it[PreferenceKeys.VISUAL_ENABLED_KEY] ?: true
                _isAudioEnabled.value = it[PreferenceKeys.AUDIO_ENABLED_KEY] ?: true
                _isVibrationEnabled.value = it[PreferenceKeys.VIBRATION_ENABLED_KEY] ?: false
            }.collect {}
        }
    }

    fun setDefaultBpm(bpm: Int) {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferenceKeys.BPM_KEY] = bpm
            }
        }
    }

    fun setVisualEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferenceKeys.VISUAL_ENABLED_KEY] = isEnabled
            }
        }
    }

    fun setAudioEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferenceKeys.AUDIO_ENABLED_KEY] = isEnabled
            }
        }
    }

    fun setVibrationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferenceKeys.VIBRATION_ENABLED_KEY] = isEnabled
            }
        }
    }
}
