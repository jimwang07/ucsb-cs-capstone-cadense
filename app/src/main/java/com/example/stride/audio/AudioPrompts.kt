package com.example.stride.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import com.example.stride.R

class AudioPrompts(private val context: Context, private val onAllLoaded: () -> Unit) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<Prompt, Int>()
    private val loadedSounds = mutableSetOf<Int>()
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    enum class Prompt {
        GET_READY, THREE, TWO, ONE, GO, CALIBRATING_PACE
    }

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM) // Using ALARM for higher priority and volume
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loadedSounds.add(sampleId)
                if (loadedSounds.size == Prompt.values().size) {
                    onAllLoaded()
                }
            }
        }

        // Preload sounds
        sounds[Prompt.GET_READY] = soundPool.load(context, R.raw.get_ready, 1)
        sounds[Prompt.THREE] = soundPool.load(context, R.raw.three, 1)
        sounds[Prompt.TWO] = soundPool.load(context, R.raw.two, 1)
        sounds[Prompt.ONE] = soundPool.load(context, R.raw.one, 1)
        sounds[Prompt.GO] = soundPool.load(context, R.raw.go, 1)
        sounds[Prompt.CALIBRATING_PACE] = soundPool.load(context, R.raw.calibrating_pace, 1)
    }

    fun play(prompt: Prompt) {
        val soundId = sounds[prompt] ?: return
        if (loadedSounds.contains(soundId)) {
            // Get current and max volume for the stream
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM).toFloat()

            // Play at max volume for the stream
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}
