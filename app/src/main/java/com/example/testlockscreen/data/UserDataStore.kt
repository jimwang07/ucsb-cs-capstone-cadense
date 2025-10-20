package com.example.testlockscreen.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDataStore(context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val DEFAULT_BPM = intPreferencesKey("default_bpm")
        val DEFAULT_VISUAL_CUE_INTERVAL = floatPreferencesKey("default_visual_cue_interval")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val TEXT_SCALE = floatPreferencesKey("text_scale")
    }

    val defaultBpm: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.DEFAULT_BPM] ?: 60
    }

    val defaultVisualCueInterval: Flow<Float> = dataStore.data.map {
        it[PreferencesKeys.DEFAULT_VISUAL_CUE_INTERVAL] ?: 1.0f
    }

    val hapticsEnabled: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.HAPTICS_ENABLED] ?: true
    }

    val textScale: Flow<Float> = dataStore.data.map {
        it[PreferencesKeys.TEXT_SCALE] ?: 1.0f
    }

    suspend fun saveSettings(bpm: Int, interval: Float, haptics: Boolean, scale: Float) {
        dataStore.edit {
            it[PreferencesKeys.DEFAULT_BPM] = bpm
            it[PreferencesKeys.DEFAULT_VISUAL_CUE_INTERVAL] = interval
            it[PreferencesKeys.HAPTICS_ENABLED] = haptics
            it[PreferencesKeys.TEXT_SCALE] = scale
        }
    }
}
