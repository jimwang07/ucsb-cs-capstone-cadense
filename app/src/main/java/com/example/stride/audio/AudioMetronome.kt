package com.example.stride.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import com.example.stride.R

class AudioMetronome(context: Context) {

    private val tag = "AudioMetronome"

    private val soundPool: SoundPool
    private var beepId: Int = 0
    private var boopId: Int = 0

    private var beepLoaded = false
    private var boopLoaded = false

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attrs)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status != 0) {
                Log.e(tag, "FAILED load: sampleId=$sampleId status=$status")
                return@setOnLoadCompleteListener
            }
            when (sampleId) {
                beepId -> beepLoaded = true
                boopId -> boopLoaded = true
            }
        }

        beepId = soundPool.load(context, R.raw.beep, 1)
        boopId = soundPool.load(context, R.raw.boop, 1)
    }

    // beatInBar: 1..4 => beep boop beep boop
    fun playBeat(beatInBar: Int) {
        val b = ((beatInBar - 1) % 4) + 1   // normalize to 1..4

        val soundId = if (b % 2 == 1) {     // 1,3
            boopId
        } else {                            // 2,4
            beepId
        }

        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }


    fun release() {
        soundPool.release()
    }
}