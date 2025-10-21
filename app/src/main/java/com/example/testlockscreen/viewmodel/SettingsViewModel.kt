package com.example.testlockscreen.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testlockscreen.data.settingsDataStore
import com.example.testlockscreen.model.VisualColorOption
import com.example.testlockscreen.model.VisualColorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.settingsDataStore

    private object PreferenceKeys {
        val BPM_KEY = intPreferencesKey("default_bpm")
        val COLOR_KEY = stringPreferencesKey("visual_color")
    }

    private val _defaultBpm = MutableStateFlow(60)
    val defaultBpm: StateFlow<Int> = _defaultBpm

    private val _visualColor = MutableStateFlow(VisualColorOptions.All.first())
    val visualColor: StateFlow<VisualColorOption> = _visualColor

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                _defaultBpm.value = preferences[PreferenceKeys.BPM_KEY] ?: 60
                val colorPref = preferences[PreferenceKeys.COLOR_KEY] ?: VisualColorOptions.All.first().preferenceValue
                _visualColor.value = VisualColorOptions.fromPreference(colorPref)
            }.collect {}
        }
    }

    fun saveSettings(bpm: Int, color: VisualColorOption) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[PreferenceKeys.BPM_KEY] = bpm
                settings[PreferenceKeys.COLOR_KEY] = color.preferenceValue
            }
        }
    }
}
