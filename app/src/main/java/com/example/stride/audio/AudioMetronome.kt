package com.example.stride.audio

import android.media.AudioManager
import android.media.ToneGenerator

class AudioMetronome {

    private var toneGenerator: ToneGenerator? = null

    init {
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 80)
    }

    fun playBeep() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
