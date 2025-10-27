package com.example.testlockscreen.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HapticsController(context: Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var vibrationJob: Job? = null

    fun startObserving(
        scope: CoroutineScope,
        isRunning: StateFlow<Boolean>,
        bpm: StateFlow<Int>
    ) {
        val observationJob = combine(isRunning, bpm) { running, currentBpm ->
            Pair(running, currentBpm)
        }
        .onEach { (running, currentBpm) ->
            vibrationJob?.cancel()
            if (running) {
                vibrationJob = scope.launch {
                    val delayMs = 60_000L / currentBpm
                    while (isActive) {
                        vibrate(30)
                        delay(delayMs)
                    }
                }
            }
        }
        .launchIn(scope)

        observationJob.invokeOnCompletion {
            vibrationJob?.cancel()
        }
    }

    private fun vibrate(milliseconds: Long) {
        // for debugging purposes only
        Log.d("HapticsController", "Vibrating for $milliseconds ms")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }
}
