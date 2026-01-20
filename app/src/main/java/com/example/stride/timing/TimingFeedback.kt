package com.example.stride.timing

sealed class TimingFeedback {
    object None : TimingFeedback()
    data class OnBeat(val offsetMs: Long) : TimingFeedback()
    data class OffBeat(val offsetMs: Long) : TimingFeedback()
}
