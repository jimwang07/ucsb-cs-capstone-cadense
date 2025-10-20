package com.example.testlockscreen.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
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
        val INTERVAL_KEY = floatPreferencesKey("default_interval")
        val HAPTICS_KEY = booleanPreferencesKey("haptics_enabled")
        val TEXT_SCALE_KEY = floatPreferencesKey("text_scale")
    }

    private val _defaultBpm = MutableStateFlow(60)
    val defaultBpm: StateFlow<Int> = _defaultBpm

    private val _defaultVisualCueInterval = MutableStateFlow(1.0f)
    val defaultVisualCueInterval: StateFlow<Float> = _defaultVisualCueInterval

    private val _hapticsEnabled = MutableStateFlow(true)
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled

    private val _textScale = MutableStateFlow(1.0f)
    val textScale: StateFlow<Float> = _textScale

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                _defaultBpm.value = preferences[PreferenceKeys.BPM_KEY] ?: 60
                _defaultVisualCueInterval.value = preferences[PreferenceKeys.INTERVAL_KEY] ?: 1.0f
                _hapticsEnabled.value = preferences[PreferenceKeys.HAPTICS_KEY] ?: true
                _textScale.value = preferences[PreferenceKeys.TEXT_SCALE_KEY] ?: 1.0f
            }.collect {}
        }
    }

    fun saveSettings(bpm: Int, interval: Float, haptics: Boolean, scale: Float) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[PreferenceKeys.BPM_KEY] = bpm
                settings[PreferenceKeys.INTERVAL_KEY] = interval
                settings[PreferenceKeys.HAPTICS_KEY] = haptics
                settings[PreferenceKeys.TEXT_SCALE_KEY] = scale
            }
        }
    }
}
