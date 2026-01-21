package com.example.stride.data.entities

data class SensorSample(
    val timestamp: Long,
    val type: String,
    val value1: Float?,
    val value2: Float?,
    val value3: Float?
)
