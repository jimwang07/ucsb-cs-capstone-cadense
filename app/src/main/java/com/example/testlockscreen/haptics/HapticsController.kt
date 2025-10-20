package com.example.testlockscreen.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HapticsController(context: Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var metronomeJob: Job? = null

    fun playMetronomeBeat(scope: CoroutineScope, bpm: Int) {
        cancelHaptics()
        metronomeJob = scope.launch {
            val delayMs = 60_000L / bpm
            while (isActive) {
                vibrate(30)
                delay(delayMs)
            }
        }
    }

    fun cancelHaptics() {
        metronomeJob?.cancel()
    }

    private fun vibrate(milliseconds: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }
}
