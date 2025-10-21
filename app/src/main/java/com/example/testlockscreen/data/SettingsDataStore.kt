package com.example.testlockscreen.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Shared DataStore entry point for app settings.
 */
val Context.settingsDataStore by preferencesDataStore("settings")
