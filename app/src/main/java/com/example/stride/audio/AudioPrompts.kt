package com.example.stride.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.example.stride.R

class AudioPrompts(private val context: Context, private val onAllLoaded: () -> Unit) {

    private val mediaPlayers = mutableMapOf<Prompt, MediaPlayer>()
    private var loadedCount = 0
    private val totalSounds = Prompt.values().size

    enum class Prompt(@RawRes val resourceId: Int) {
        GET_READY(R.raw.get_ready),
        THREE(R.raw.three),
        TWO(R.raw.two),
        ONE(R.raw.one),
        GO(R.raw.go),
        CALIBRATING_PACE(R.raw.calibrating_pace)
    }

    init {
        Prompt.values().forEach { prompt ->
            val mediaPlayer = MediaPlayer.create(context, prompt.resourceId)
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            mediaPlayer.setOnPreparedListener {
                loadedCount++
                if (loadedCount == totalSounds) {
                    onAllLoaded()
                }
            }
            mediaPlayers[prompt] = mediaPlayer
        }
    }

    fun play(prompt: Prompt) {
        mediaPlayers[prompt]?.start()
    }

    fun release() {
        mediaPlayers.values.forEach { it.release() }
    }
}
