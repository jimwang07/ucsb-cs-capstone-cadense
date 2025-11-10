package com.example.testlockscreen.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testlockscreen.data.entities.SensorSample
import com.example.testlockscreen.sensors.SensorManagerWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImuGraphViewModel(application: Application) : AndroidViewModel(application) {

    private val sensorManagerWrapper = SensorManagerWrapper(application)

    private val _samples = MutableStateFlow<List<SensorSample>>(emptyList())
    val samples: StateFlow<List<SensorSample>> = _samples.asStateFlow()

    private val sampleBufferSize = 200

    init {
        viewModelScope.launch {
            sensorManagerWrapper.getAccelerometerSensorFlow().collect { sample ->
                _samples.update { current ->
                    val trimmed = if (current.size >= sampleBufferSize) {
                        current.drop(current.size - sampleBufferSize + 1)
                    } else {
                        current
                    }
                    trimmed + sample
                }
            }
        }
    }
}
