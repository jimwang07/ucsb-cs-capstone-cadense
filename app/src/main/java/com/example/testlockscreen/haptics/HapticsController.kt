package com.example.testlockscreen.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticsController(context: Context) {

    private val vibrator: Vibrator

    init {
        vibrator = if (Build.VERSION_CODES.S <= Build.VERSION.SDK_INT) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun playMetronomeBeat() {
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    fun playVisualCueHaptic() {
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    fun cancelHaptics() {
        vibrator.cancel()
    }
}
