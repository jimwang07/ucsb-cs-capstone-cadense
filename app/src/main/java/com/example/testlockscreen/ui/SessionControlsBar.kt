package com.example.testlockscreen.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun SessionControlsBar(
    state: MainViewModel.SessionState,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onEnd: () -> Unit,
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                onStart()
                vibrate(vibrator, 50)
            },
            enabled = state != MainViewModel.SessionState.Running,
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize)
        ) {
            Text("Start")
        }

        Button(
            onClick = onStop,
            enabled = state == MainViewModel.SessionState.Running,
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize)
        ) {
            Text("Stop")
        }

        Button(
            onClick = {
                onEnd()
                vibrate(vibrator, 200)
            },
            enabled = state != MainViewModel.SessionState.Idle,
            colors = ButtonDefaults.secondaryButtonColors(),
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize)
        ) {
            Text("End")
        }
    }
}

private fun vibrate(vibrator: Vibrator, milliseconds: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(milliseconds)
    }
}
