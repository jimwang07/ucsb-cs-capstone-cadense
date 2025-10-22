package com.example.testlockscreen.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class AudioMetronome {

    private var toneGenerator: ToneGenerator? = null
    private var job: Job? = null
    private val delayMs = AtomicLong(1000L) // Default to 60 BPM (1000ms)

    fun start(scope: CoroutineScope, initialBpm: Int) {
        // Prevent multiple instances from running
        if (job?.isActive == true) return

        updateBpm(initialBpm)
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 80)
        job = scope.launch {
            while (isActive) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                delay(delayMs.get())
            }
        }
    }

    fun updateBpm(bpm: Int) {
        if (bpm > 0) {
            val newDelay = (60_000.0 / bpm).toLong().coerceAtLeast(100L)
            delayMs.set(newDelay)
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        toneGenerator?.release()
        toneGenerator = null
    }
}