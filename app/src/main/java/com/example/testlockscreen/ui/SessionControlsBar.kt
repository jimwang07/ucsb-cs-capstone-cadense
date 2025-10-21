package com.example.testlockscreen.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun SessionControlsBar(
    state: MainViewModel.SessionState,
    onHome: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onEnd: () -> Unit,
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ControlChip(
            label = "Home",
            colors = ChipDefaults.secondaryChipColors(),
            onClick = onHome
        )
        ControlChip(
            label = "Start",
            enabled = state != MainViewModel.SessionState.Running,
            colors = ChipDefaults.primaryChipColors(),
            onClick = {
                onStart()
                vibrate(vibrator, 50)
            }
        )
        ControlChip(
            label = "Pause",
            enabled = state == MainViewModel.SessionState.Running,
            colors = ChipDefaults.secondaryChipColors(),
            onClick = onStop
        )
        ControlChip(
            label = "End",
            enabled = state != MainViewModel.SessionState.Idle,
            colors = ChipDefaults.secondaryChipColors(),
            onClick = {
                onEnd()
                vibrate(vibrator, 200)
            }
        )
    }
}

@Composable
private fun ControlChip(
    label: String,
    enabled: Boolean = true,
    colors: androidx.wear.compose.material.ChipColors,
    onClick: () -> Unit
) {
    androidx.wear.compose.material.Chip(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun vibrate(vibrator: Vibrator, milliseconds: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(milliseconds)
    }
}
