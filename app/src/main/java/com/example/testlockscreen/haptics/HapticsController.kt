package com.example.testlockscreen.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class HapticsController(context: Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate(milliseconds: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }
    
    fun cancel() {
        vibrator.cancel()
    }
}