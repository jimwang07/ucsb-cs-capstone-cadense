package com.example.stride.audio

import android.media.AudioManager
import android.media.ToneGenerator

class AudioMetronome {

    private var toneGenerator: ToneGenerator? = null

    init {
        // Increased volume to 100 (MAX)
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    }

    fun playBeep() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
