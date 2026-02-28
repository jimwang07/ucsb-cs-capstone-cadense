package com.example.stride.sensors

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A utility to manually trigger pole strike events, primarily for testing on the emulator.
 */
object ManualStrikeTrigger {
    private val _events = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun trigger() {
        _events.tryEmit(Unit)
    }
}
