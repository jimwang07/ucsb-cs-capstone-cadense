package com.example.testlockscreen.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioMetronome {

    private var toneGenerator: ToneGenerator? = null
    private var job: Job? = null

    fun start(scope: CoroutineScope, bpm: Int) {
        stop()
        if (bpm <= 0) return
        val delayMs = (60_000.0 / bpm).toLong().coerceAtLeast(100L)
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 80)
        job = scope.launch {
            while (isActive) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                delay(delayMs)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        toneGenerator?.release()
        toneGenerator = null
    }
}
